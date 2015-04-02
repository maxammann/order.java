package order;

/**
 * Unchecked version of {@link ArgumentException}
 */
public class ArgumentRuntimeException extends RuntimeException {

    public ArgumentRuntimeException() {
    }

    public ArgumentRuntimeException(String message, Object... args) {
        this(null, message, args);
    }

    public ArgumentRuntimeException(Throwable cause, String message, Object... args) {
        super(args.length == 0 ? message : String.format(message, args), cause);
    }

    public ArgumentRuntimeException(Throwable cause) {
        super(cause);
    }
}
