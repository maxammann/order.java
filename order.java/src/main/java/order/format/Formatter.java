package order.format;

/**
 *
 */
public interface Formatter {
    void cropRight(StringBuilder text, double length);

    void cropLeft(StringBuilder text, double length);


    void padLeft(StringBuilder text, double textLength, double length);

    void padRight(StringBuilder text, double textLength, double length);


    void center(StringBuilder text, double textLength, double length);

    void fill(StringBuilder text, double textLength, char c);
}
