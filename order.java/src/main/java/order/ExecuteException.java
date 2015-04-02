package order;

/**
 * Thrown when something goes wrong during command execution.
 */
public class ExecuteException extends CommandException {

    public ExecuteException(CommandContext context) {
        super(context);
    }

    public ExecuteException(String message, CommandContext context, Object... args) {
        super(message, context, args);
    }

    public ExecuteException(Throwable cause, String message, CommandContext context, Object... args) {
        super(cause, message, context, args);
    }

    public ExecuteException(Throwable cause, CommandContext context) {
        super(cause, context);
    }
}
