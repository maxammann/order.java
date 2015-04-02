package order;

import order.sender.Sender;

/**
 * Resolves {@link Command}s by an identifier and argument amount.
 */
public interface CommandResolver<S extends Sender> {

    /**
     * Finds a command in a command source.
     * If the identifier is not found this fails immediately.
     * <p/>
     * If there are results for the identifier but the arguments do not match then return the best matching command.
     *
     * @param identifier The identifier
     * @param arguments  The arguments
     * @return The found command
     */
    Command<S> find(String identifier, int arguments);
}
