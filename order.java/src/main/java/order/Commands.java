package order;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import order.sender.Sender;
import order.token.TokenResult;
import order.token.Tokenizer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Represents the root command. All commands which can be executed will be contained in
 * this {@link GroupCommand}.
 */
public class Commands<S extends Sender> extends GroupCommand<S> {

    private final Tokenizer tokenizer;
    private final Thread.UncaughtExceptionHandler handler;


    @Inject
    public Commands(Tokenizer tokenizer,
                    @Named("root-name") String name,
                    @Named("root-description") String description,
                    @Named("sync-executor") ListeningExecutorService syncService,
                    @Named("command-exception-handler") Thread.UncaughtExceptionHandler handler,
                    @Named("group-help-executor") Executor<S> helpExecutor,
                    @Named("commands") Set<Command<S>> commands,
                    CommandPipeline<S> pipeline) {
        super(name, "", description, pipeline.build(helpExecutor), pipeline
                .build(helpExecutor), syncService, DEFAULT_SENDER);
        this.tokenizer = tokenizer;
        this.handler = handler;

        for (Command<S> command : commands) {
            addChild(command);
        }
    }

    public ListenableFuture<CommandContext<S>> execute(S sender, String identifier, String[] args) {
        return execute(sender, tokenizer.tokenize(identifier, args));
    }

    public ListenableFuture<CommandContext<S>> execute(S sender, String input) {
        return execute(sender, tokenizer.tokenize(input));
    }

    public CommandContext<S> createContext(S sender, String identifier, String[] args) {
        return createContext(sender, tokenizer.tokenize(identifier, args));
    }

    private ListenableFuture<CommandContext<S>> execute(S sender, final TokenResult result) {
        final CommandContext<S> ctx = createContext(sender, result);
        final Command<S> command = ctx.getCommand();

        ListeningExecutorService service = command.getService();

        ListenableFuture<CommandContext<S>> future = service.submit(new Callable<CommandContext<S>>() {
            @Override
            public CommandContext<S> call() throws Exception {
                try {
                    return command.parse(ctx, result);
                } catch (ParsingException e) {
                    handler.uncaughtException(Thread.currentThread(), e);
                }

                return null;
            }
        });

        Futures.addCallback(future, new FutureCallback<CommandContext<S>>() {
            @Override
            public void onSuccess(CommandContext<S> result) {
                if (result == null) {
                    return;
                }
                try {
                    result.execute();
                } catch (ExecuteException e) {
                    handler.uncaughtException(Thread.currentThread(), e);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                handler.uncaughtException(Thread.currentThread(), t);
            }
        }, service);

        return future;
    }
}
