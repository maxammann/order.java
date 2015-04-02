package order.example;

import order.CommandContext;
import order.Executor;
import order.reflect.Argument;
import order.reflect.Command;
import order.reflect.Option;
import order.sender.Sender;

/**
 * Represents a Example
 */

@Command(identifier = "list", description = "test")
public class Example implements Executor<Sender> {

    @Argument(name = "oh")
    public int oh;

    @Option(name = "yes")
    public int yes;

    @Override
    public void execute(CommandContext<Sender> ctx, Sender sender) {
        System.out.println("A " + yes + " Fucking oh example!" + oh);
    }
}
