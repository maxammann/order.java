package order.parser;

import order.CommandContext;
import order.ParsingException;
import org.jetbrains.annotations.Nullable;

/**
 * Used to parsing a {@link java.lang.String} to another java value.
 */
public interface ArgumentParser<T> {

    @Nullable
    T parse(String input, CommandContext<?> ctx) throws ParsingException;
}
