package order.format.table;

/**
 *
 */
public interface RowForwarder {

    int getColumns();

    String getColumn(int column);
}
