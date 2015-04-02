package order.format.table;

import order.format.WidthProvider;
import order.format.pagination.PaginateResult;
import order.format.pagination.Paginator;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.MessageFormat;
import java.util.List;

/**
 * Represents a FormattedTable
 */
public class FormattedTable extends DefaultTable {

    private final String header;

    @Inject
    public FormattedTable(WidthProvider widthProvider,
                          RowFactory rowFactory,
                          Paginator paginator,
                          @Named("table-header") String header,
                          @Named("padding") int padding,
                          @Named("append") String append) {
        super(widthProvider, rowFactory, paginator, padding, append);
        this.header = header;
    }

    @Override
    protected void output(String name, StringBuilder builder, PaginateResult<Row> result, List<Row> rows) {
        builder.append(MessageFormat.format(header, result.page(), result.getMaxPages(), rows.size(), name))
                .append('\n');
    }
}
