package order.format;

/**
 * Represents a CharacterWidthProvider
 */
public interface WidthProvider {

    double widthOf(String string);

    double widthOf(StringBuilder string);

    double widthOf(char c);
}
