package order.parser;

import order.CommandContext;
import order.ParsingException;

/**
 * Represents a StringParser
 */
public class StringParser implements ArgumentParser<String> {

    @Override
    public String parse(String input, CommandContext<?> ctx) throws ParsingException {
        return input;
    }
}
