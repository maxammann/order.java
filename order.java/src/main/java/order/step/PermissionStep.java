package order.step;

import order.Command;
import order.CommandContext;
import order.ExecuteException;
import order.Executor;
import order.sender.Sender;

/**
 * Represents a PermissionStep
 */
public class PermissionStep implements Executor<Sender> {


    @Override
    public void execute(CommandContext<Sender> ctx, Sender sender) throws ExecuteException {
        Command<Sender> command = ctx.getCommand();

        if (!sender.hasPermission(command)) {
            ctx.cancel();
            sender.send("no-permission");
        }
    }
}
