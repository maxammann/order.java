package order.parser;

import order.CommandContext;
import order.ParsingException;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Represents a IntegerParser
 */
public class NumberParser implements ArgumentParser<Number> {

    public static final NumberFormat FORMAT = NumberFormat.getInstance();

    @Override
    public Number parse(String input, CommandContext<?> ctx) throws ParsingException {
        try {
            return FORMAT.parse(input);
        } catch (ParseException e) {
            throw new ParsingException(e, ctx);
        }
    }
}
