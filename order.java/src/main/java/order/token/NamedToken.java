package order.token;

/**
 * Represents a NamedToken
 */
public class NamedToken extends Token {

    private final String name;

    public NamedToken(String name, String value) {
        super(value);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
