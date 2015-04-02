package order;

import com.google.common.collect.Lists;
import order.sender.Sender;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

/**
 * Represents a AbstractCommandPipeline
 */
public class DefaultCommandPipeline<S extends Sender> implements CommandPipeline<S> {

    private final Thread.UncaughtExceptionHandler exceptionHandler;
    private ArrayList<Executor<S>> after = Lists.newArrayList(), before = Lists.newArrayList();


    @Inject
    public DefaultCommandPipeline(@Named("command-exception-handler") Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }


//    @Inject(optional = true)
//    public void after(@Named("pipeline-after") Set<Executor<S>> executors) {
//        after.addAll(executors);
//    }
//
//    @Inject(optional = true)
//    public void before(@Named("pipeline-before") Set<Executor<S>> executors) {
//        before.addAll(executors);
//    }

    @Override
    public void addAfter(Executor<S> executor) {
        after.add(executor);
    }

    @Override
    public void addBefore(Executor<S> executor) {
        before.add(executor);
    }

    @Override
    public Executor<S> build(final Executor<S> executor) {
        return new Executor<S>() {
            @Override
            public void execute(CommandContext<S> ctx, S sender) throws ExecuteException {
                for (Executor<S> executor : before) {
                    try {
                        executor.execute(ctx, sender);
                    } catch (Exception e) {
                        exceptionHandler.uncaughtException(Thread.currentThread(), e);
                    }

                    if (ctx.isCancelled()) {
                        return;
                    }
                }

                try {
                    executor.execute(ctx, sender);
                } catch (Exception e) {
                    exceptionHandler.uncaughtException(Thread.currentThread(), e);
                }

                if (ctx.isCancelled()) {
                    return;
                }

                for (Executor<S> executor : after) {
                    try {
                        executor.execute(ctx, sender);
                    } catch (Exception e) {
                        exceptionHandler.uncaughtException(Thread.currentThread(), e);
                    }
                    if (ctx.isCancelled()) {
                        return;
                    }
                }
            }
        };
    }
}
