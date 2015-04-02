package order.format.table;

/**
 *
 */
public interface Table {

    Row addRow(Object... sections);

    Row addRow(Row row);

    void addRow(Row row, Align... aligns);

    Row addForwardingRow(RowForwarder forwarder);

    double getMaxWidth(int col);

    String render(String name, int page);
}
