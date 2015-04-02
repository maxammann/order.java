package order;

import order.parser.ArgumentParser;

/**
 * Represents a required argument or an optional option if {@link #isOption()} returns true.
 */
public class Argument<T> implements ArgumentParser<T> {

    private final String internalName;
    private final String name;
    private final String description;
    private final boolean option;
    private final ArgumentParser<T> parser;

    public Argument(String internalName, String name, String description, boolean option, ArgumentParser<T> parser) {
        this.internalName = internalName;
        this.name = name;
        this.description = description;
        this.option = option;
        this.parser = parser;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getName() {
        return name;
    }

    public boolean isOption() {
        return option;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public T parse(String input, CommandContext<?> ctx) throws ParsingException {
        return parser.parse(input, ctx);
    }

    @Override
    public String toString() {
        return "Argument{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", option=" + option +
                '}';
    }
}
