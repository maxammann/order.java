package order;

import order.sender.Sender;

/**
 * A {@link Executor} is used commands. An executor can also forward a command execution.
 */
public interface Executor<S extends Sender> {

    void execute(CommandContext<S> ctx, S sender) throws ExecuteException;
}
