package order.parser;

import order.CommandContext;
import order.ParsingException;

import java.io.File;

/**
 * Represents a IntegerParser
 */
public class FileParser implements ArgumentParser<File> {

    @Override
    public File parse(String input, CommandContext<?> ctx) throws ParsingException {
        try {
            return new File(input);
        } catch (NumberFormatException e) {
            throw new ParsingException(e, ctx);
        }
    }
}
