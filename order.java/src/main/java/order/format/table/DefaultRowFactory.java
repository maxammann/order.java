package order.format.table;

import order.Translate;
import order.format.Formatter;
import order.format.WidthProvider;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Represents a DefaultRowFactory
 */
public class DefaultRowFactory implements RowFactory {

    private final Formatter formatter;
    private final WidthProvider widthProvider;
    private final double columnSpacing;
    private final double maxLineLength;
    private final Translate translate;

    @Inject
    public DefaultRowFactory(Formatter formatter, WidthProvider widthProvider,
                             @Named("column-spacing") double columnSpacing,
                             @Named("max-line-length") double maxLineLength, Translate translate) {
        this.formatter = formatter;
        this.widthProvider = widthProvider;
        this.columnSpacing = columnSpacing;
        this.maxLineLength = maxLineLength;
        this.translate = translate;
    }


    @Override
    public Row create(RowForwarder forward) {
        return new ForwardingRow(formatter, widthProvider, columnSpacing, maxLineLength, forward);
    }

    @Override
    public Row create(Object... columns) {
        return new DefaultRow(formatter, widthProvider, columnSpacing, maxLineLength, columns);
    }

    @Override
    public Row translated(boolean capitalize, Object... columns) {
        return new DictionaryRow(formatter, widthProvider, columnSpacing, maxLineLength, translate, capitalize, columns);
    }
}
