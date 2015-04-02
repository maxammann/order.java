package order;

/**
 * Thrown when something wrong happens with commands.
 */
public class CommandException extends ArgumentRuntimeException {
    private final CommandContext context;


    public CommandException(CommandContext context) {
        this.context = context;
    }

    public CommandException(String message, CommandContext context, Object... args) {
        super(message, args);
        this.context = context;
    }

    public CommandException(Throwable cause, String message, CommandContext context, Object... args) {
        super(cause, message, args);
        this.context = context;
    }

    public CommandException(Throwable cause, CommandContext context) {
        super(cause);
        this.context = context;
    }

    public CommandContext getContext() {
        return context;
    }
}
