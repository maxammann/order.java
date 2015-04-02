package order.format.table;

import order.format.Formatter;
import order.format.WidthProvider;

/**
 * Represents a AbstractRow
 */
public abstract class AbstractRow implements Row {

    private Align[] alignment = null;
    private final double columnSpacing;
    private final double maxLineLength;

    private final Formatter formatter;
    private final WidthProvider widthProvider;

    public AbstractRow(Formatter formatter,
                       WidthProvider widthProvider,
                       double columnSpacing,
                       double maxLineLength) {
        this.columnSpacing = columnSpacing;
        this.maxLineLength = maxLineLength;
        this.formatter = formatter;
        this.widthProvider = widthProvider;
    }

    @Override
    public StringBuilder render(Table table) {
        StringBuilder finalRow = new StringBuilder();

        for (int column = 0; column < getColumns(); column++) {
            String section = getColumn(column);
            double columnSize = getColumnSize(table.getMaxWidth(column));
            Align align = getAlign(column);

            if (align == null) {
                align = DEFAULT_ALIGN;
            }

            StringBuilder sectionBuilder = new StringBuilder(section);

            double sectionLength = widthProvider.widthOf(sectionBuilder);

            switch (align) {
                case RIGHT:
                    if (sectionLength > columnSize) {
                        formatter.cropLeft(sectionBuilder, columnSize);
                    } else if (sectionLength < columnSize) {
                        formatter.padLeft(sectionBuilder, sectionLength, columnSize);
                    }
                    break;
                case LEFT:
                    if (sectionLength > columnSize) {
                        formatter.cropRight(sectionBuilder, columnSize);
                    } else if (sectionLength < columnSize) {
                        formatter.padRight(sectionBuilder, sectionLength, columnSize);
                    }
                    break;
                case CENTER:
                    if (sectionLength > columnSize) {
                        formatter.cropRight(sectionBuilder, columnSize);
                    } else if (sectionLength < columnSize) {
                        formatter.center(sectionBuilder, sectionLength, columnSize);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Align not found!");
            }

            finalRow.append(sectionBuilder);
        }

        formatter.cropRight(finalRow, maxLineLength);

        return finalRow;
    }

    private double getColumnSize(double maxWidth) {
        return maxWidth + columnSpacing;
    }

    /**
     * Sets the alignment of each row.
     * <p/>
     * <p>Example:</p>
     * <p/>
     * block.setAlignment(Align.LEFT, Align.RIGHT);
     * <p/>
     * <p>This will produce something like this:</p>
     * <p/>
     * |TestString    |    TextString|
     *
     * @param alignment An array of alignments
     */
    @Override
    public void setAlignment(Align... alignment) {
        this.alignment = alignment;
    }

    /**
     * Gets the align of the column.
     *
     * @param column The column.
     * @return The align of this column.
     */
    @Override
    public Align getAlign(int column) {
        if (alignment == null) {
            return null;
        }

        return alignment[column];
    }
}
