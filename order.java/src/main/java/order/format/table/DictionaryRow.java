package order.format.table;

import order.Translate;
import order.format.Formatter;
import order.format.WidthProvider;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Represents a DictionaryRow
 */
public class DictionaryRow extends AbstractRow {
    private final Translate dictionary;
    private final boolean capitalize;
    private final Object[] columns;

    @Inject
    public DictionaryRow(Formatter formatter,
                         WidthProvider widthProvider,
                         @Named("column-spacing") double columnSpacing,
                         @Named("max-line-length") double maxLineLength,
                         Translate dictionary,
                         boolean capitalize, Object... columns) {
        super(formatter, widthProvider, columnSpacing, maxLineLength);
        this.dictionary = dictionary;
        this.capitalize = capitalize;
        this.columns = columns.clone();
    }

    @Override
    public int getColumns() {
        return columns.length;
    }

    @Override
    public String getColumn(int column) {
        String string = columns[column].toString();
        String result = dictionary.getTranslation(string);
        return capitalize ? capitalize(result) : result;
    }

    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        return String.valueOf(Character.toTitleCase(str.charAt(0))) + str.substring(1);
    }
}
