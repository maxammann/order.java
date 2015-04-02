package order.format.table;

/**
 *
 */
public interface Row extends RowForwarder {
    Align DEFAULT_ALIGN = Align.LEFT;

    CharSequence render(Table table);

    void setAlignment(Align... alignment);

    Align getAlign(int column);
}
