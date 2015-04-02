package order.parser;

import order.CommandContext;
import order.ParsingException;

/**
 * Represents a IntegerParser
 */
public class IntegerParser implements ArgumentParser<Integer> {

    @Override
    public Integer parse(String input, CommandContext<?> ctx) throws ParsingException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ParsingException(e, ctx);
        }
    }
}
