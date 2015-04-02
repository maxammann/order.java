package order.format.table;

import order.format.Formatter;
import order.format.WidthProvider;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Represents a ForwardingRow
 */
public class ForwardingRow extends AbstractRow {

    private final RowForwarder forwarded;

    @Inject
    public ForwardingRow(Formatter formatter,
                         WidthProvider widthProvider,
                         @Named("column-spacing") double columnSpacing,
                         @Named("max-line-length") double maxLineLength,
                         RowForwarder forwarded) {
        super(formatter, widthProvider, columnSpacing, maxLineLength);
        this.forwarded = forwarded;
    }

    @Override
    public int getColumns() {
        return forwarded.getColumns();
    }

    @Override
    public String getColumn(int column) {
        String content = forwarded.getColumn(column);

        if (content == null) {
            return "null";
        }

        return content;
    }
}
