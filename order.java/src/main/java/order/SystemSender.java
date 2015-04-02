package order;

import order.sender.Sender;

import java.text.MessageFormat;

/**
 * This represents a {@link order.sender.Sender} which uses the {@link System#out} stream
 * to output messages
 */
public class SystemSender implements Sender {
    @Override
    public String getName() {
        return "system";
    }

    @Override
    public void send(String message) {
        System.out.println(message);
    }

    @Override
    public void send(String message, Object... args) {
        send(MessageFormat.format(message, args));
    }

    @Override
    public void send(StringBuilder message) {
        send(message.toString());
    }

    @Override
    public final boolean hasPermission(Command command) {
        return true;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
