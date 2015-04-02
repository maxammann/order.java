package order.format;

/**
 * Represents a MonospacedWidthProvider
 */
public class MonospacedWidthProvider implements WidthProvider {


    @Override
    public double widthOf(String string) {
        return string.length();
    }

    @Override
    public double widthOf(StringBuilder string) {
        return string.length();
    }

    @Override
    public double widthOf(char c) {
        return 1;
    }
}
