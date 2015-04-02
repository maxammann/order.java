package order.format.table;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import order.*;
import order.builder.GroupBuilder;
import order.sender.Sender;
import order.token.DelimiterTokenizer;
import order.token.SpaceDelimiter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

@RunWith(JUnit4.class)
public class CommandTest {
    private final Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {

        }
    };
    private ListeningExecutorService executorService = MoreExecutors
            .listeningDecorator(MoreExecutors.newDirectExecutorService());

    private final Executor<Sender> helpExecutor = new HelpExecutor();
    private final DelimiterTokenizer tokenizer = new DelimiterTokenizer(new SpaceDelimiter());

    private final Commands<Sender> controller = new Commands<Sender>(tokenizer, "Test", "Test", executorService, new TestUncaughtExceptionHandler(), helpExecutor, Collections.<Command<Sender>>emptySet(), new DefaultCommandPipeline<Sender>(exceptionHandler));

    private final Sender sender = new SystemSender();


    @Before
    public void setUp() throws Exception {
        GroupBuilder<Sender> builder = new GroupBuilder<Sender>(executorService, new DefaultHelpExecutor<Sender>(), new DefaultCommandPipeline<Sender>(exceptionHandler));
        builder.identifier("group").name("group").description("A fucking group!");

        GroupCommand<Sender> parent = builder.build();

        ExecutableCommand<Sender> child = new ExecutableCommand<Sender>("fucking", "fucking", "Fucking test!", new Executor<Sender>() {
            @Override
            public void execute(CommandContext<Sender> ctx, Sender sender) {
                sender.send("A Fucking success of " + ctx.get("?") + " bitches!");
            }
        }, helpExecutor, executorService, Command.DEFAULT_SENDER);

        parent.addChild(child);

        controller.addChild(parent);
    }

    @Test
    public void testParse() throws Exception {
        controller.execute(sender, "group fucking").get();
    }

    private static class TestUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
        }
    }

    private static class HelpExecutor implements Executor<Sender> {
        @Override
        public void execute(CommandContext<Sender> ctx, Sender sender) {
            System.out.println(ctx.getCommand().getClass().getSimpleName());
        }
    }
}
