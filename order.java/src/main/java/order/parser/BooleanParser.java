package order.parser;

import order.CommandContext;
import order.ParsingException;

/**
 * Represents a IntegerParser
 */
public class BooleanParser implements ArgumentParser<Boolean> {

    @Override
    public Boolean parse(String input, CommandContext<?> ctx) throws ParsingException {
        if (input == null || input.isEmpty()) {
            return true;
        }

        return Boolean.valueOf(input);
    }
}
