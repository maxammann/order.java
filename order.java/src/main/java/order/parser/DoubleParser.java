package order.parser;

import order.CommandContext;
import order.ParsingException;

/**
 * Represents a DoubleParser
 */
public class DoubleParser implements ArgumentParser<Double> {

    @Override
    public Double parse(String input, CommandContext<?> ctx) throws ParsingException {
        if (input == null || input.isEmpty()) {
            return Double.NaN;
        }

        return Double.valueOf(input);
    }
}
