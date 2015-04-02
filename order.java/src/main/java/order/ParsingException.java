package order;

/**
 * Thrown when something goes wrong while parsing a command input
 */
public class ParsingException extends CommandException {

    public ParsingException(CommandContext context) {
        super(context);
    }

    public ParsingException(String message, CommandContext context, Object... args) {
        super(message, context, args);
    }

    public ParsingException(Throwable cause, String message, CommandContext context, Object... args) {
        super(cause, message, context, args);
    }

    public ParsingException(Throwable cause, CommandContext context) {
        super(cause, context);
    }
}
