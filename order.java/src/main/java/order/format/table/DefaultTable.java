package order.format.table;


import order.format.WidthProvider;
import order.format.pagination.PaginateResult;
import order.format.pagination.Paginator;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultTable implements Table {

    private List<Row> rows = new ArrayList<Row>();

    private final WidthProvider widthProvider;
    private final RowFactory rowFactory;
    private final Paginator paginator;
    private final String append;

    private final String padding;
    private final HashMap<Integer, Double> maxColumnSpaces = new HashMap<Integer, Double>();

    @Inject
    public DefaultTable(WidthProvider widthProvider,
                        RowFactory rowFactory,
                        Paginator paginator,
                        @Named("padding") int padding,
                        @Named("append") String append) {
        this.widthProvider = widthProvider;
        this.rowFactory = rowFactory;
        this.paginator = paginator;
        this.append = append;
        this.padding = repeat(" ", padding);
    }

    private String repeat(String str, int repeat) {
        StringBuilder builder = new StringBuilder(repeat * str.length());

        for (int i = 0; i < repeat; i++) {
            builder.append(str);
        }

        return builder.toString();
    }

    @Override
    public Row addRow(Object... sections) {
        return addRow(rowFactory.create(sections));
    }

    @Override
    public Row addRow(Row row) {
        rows.add(row);

        for (int i = 0, length = row.getColumns(); i < length; i++) {
            String column = row.getColumn(i);


            Double width = maxColumnSpaces.get(i);
            if (width != null) {
                maxColumnSpaces.put(i, Math.max(width, widthProvider.widthOf(column)));
            } else {
                maxColumnSpaces.put(i, widthProvider.widthOf(column));
            }
        }

        return row;
    }

    @Override
    public void addRow(Row row, Align... aligns) {
        row.setAlignment(aligns);
        addRow(row);
    }

    @Override
    public Row addForwardingRow(RowForwarder forwarder) {
        return addRow(rowFactory.create(forwarder));
    }

    @Override
    public String render(String name, int page) {
        PaginateResult<Row> result = paginator.paginate(rows, page);

        List<Row> rows = result.result();

        if (rows.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        output(name, builder, result, rows);

        for (Row row : rows) {
            builder.append(padding).append(row.render(this)).append(append).append('\n');
        }

        return builder.toString();
    }

    protected void output(String name, StringBuilder builder, PaginateResult<Row> result, List<Row> rows) {

    }

    @Override
    public double getMaxWidth(int column) {
        return maxColumnSpaces.get(column);
    }

}
