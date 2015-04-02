package order.example;

import order.CommandContext;
import order.Executor;
import order.reflect.Command;
import order.reflect.instance.Children;
import order.sender.Sender;

/**
 * Represents a ParentExample
 */
@Command(identifier = "clan", description = "test")
@Children({Example.class})
public class ParentExample implements Executor<Sender> {
    @Override
    public void execute(CommandContext<Sender> ctx, Sender sender) {

    }
}
