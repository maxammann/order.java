package order.format;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Represents a DefaultFormatter
 */
public class DefaultFormatter implements Formatter {

    private final WidthProvider provider;
    private final double maxLength;

    @Inject
    public DefaultFormatter(WidthProvider provider, @Named("max-line-length") double maxLength) {
        this.provider = provider;
        this.maxLength = maxLength;
    }

    /**
     * Crops the string right in the {@link StringBuilder}.
     *
     * @param text   The message to crop right.
     * @param length The length it of the section it should be crop right.
     */
    @Override
    public void cropRight(StringBuilder text, double length) {
        if (text == null) {
            throw new IllegalArgumentException("The text can not be null!");
        }

        while (provider.widthOf(text) >= length) {
            text.deleteCharAt(text.length() - 1);
        }
    }

    /**
     * Crops the string left in the {@link StringBuilder}.
     *
     * @param text   The message to crop left.
     * @param length The length it of the section it should be crop left.
     */
    @Override
    public void cropLeft(StringBuilder text, double length) {
        if (text == null) {
            throw new IllegalArgumentException("The text can not be null!");
        }

        while (provider.widthOf(text) >= length) {
            text.deleteCharAt(0);
        }
    }

    /**
     * Pads the string right in the {@link StringBuilder}.
     *
     * @param text   The message to pad right.
     * @param length The length it of the section it should be pad right.
     */
    @Override
    public void padRight(StringBuilder text, double textLength, double length) {
        padRight(text, textLength, length, ' ');
    }

    public void padRight(StringBuilder text, double textLength, double length, char padChar) {
        if (text == null) {
            throw new IllegalArgumentException("The text can not be null!");
        }

        if (textLength > length) {
            return;
        }

        double charWidth = provider.widthOf(padChar);

        for (; textLength + charWidth < length; textLength += charWidth) {
            text.append(padChar);
        }
    }

    /**
     * Pads the string left in the {@link StringBuilder}.
     *
     * @param text   The message to pad left.
     * @param length The length it of the section it should be pad left.
     */
    @Override
    public void padLeft(StringBuilder text, double textLength, double length) {
        if (text == null) {
            throw new IllegalArgumentException("The text can not be null!");
        }

        if (textLength > length) {
            return;
        }

        char padChar = ' ';
        double charWidth = provider.widthOf(padChar);

        for (; textLength < length; textLength += charWidth) {
            text.insert(0, padChar);
        }
    }

    /**
     * Centers the string in the {@link StringBuilder}.
     *
     * @param text       The message to center.
     * @param lineLength The length it of the section it should be centered in.
     */
    @Override
    public void center(StringBuilder text, double textLength, double lineLength) {
        double diff = lineLength - textLength;

        // if too big for line return it as is

        if (diff < 0) {
            return;
        }

        double sideSpace = diff / 2;

        // pad the left with space

        padLeft(text, textLength, textLength + sideSpace);
    }

    @Override
    public void fill(StringBuilder text, double textLength, char c) {
        padRight(text, textLength, maxLength, c);
    }
}
