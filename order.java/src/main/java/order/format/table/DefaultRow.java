package order.format.table;


import order.format.Formatter;
import order.format.WidthProvider;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Represents a Row
 */
public final class DefaultRow extends AbstractRow {


    private final Object[] columns;

    @Inject
    public DefaultRow(Formatter formatter,
                      WidthProvider widthProvider,
                      @Named("column-spacing") double columnSpacing,
                      @Named("max-line-length") double maxLineLength,
                      Object... columns) {
        super(formatter, widthProvider, columnSpacing, maxLineLength);
        this.columns = columns.clone();
    }

    @Override
    public int getColumns() {
        return columns.length;
    }

    @Override
    public String getColumn(int column) {
        Object obj = columns[column];
        if (obj == null) {
            return "null";
        }
        return obj.toString();
    }
}
