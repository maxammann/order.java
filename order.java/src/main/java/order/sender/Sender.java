package order.sender;

import order.Command;

/**
 * Represents a Sender
 */
public interface Sender {

    String getName();

    void send(String message);

    void send(String message, Object... args);

    void send(StringBuilder message);

    boolean hasPermission(Command command);

    boolean hasPermission(String permission);

    interface Executor<S extends Sender, R> {

        R execute(S sender);

        R defaultValue(Sender sender);
    }
}
