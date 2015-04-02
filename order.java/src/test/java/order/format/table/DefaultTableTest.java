package order.format.table;

import order.NaughtyTranslate;
import order.format.DefaultFormatter;
import order.format.Formatter;
import order.format.MonospacedWidthProvider;
import order.format.WidthProvider;
import order.format.pagination.DummyPaginator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;

@RunWith(JUnit4.class)
public class DefaultTableTest {

    @Test
    public void testRender() throws Exception {
        final WidthProvider widthProvider = new MonospacedWidthProvider();
        final Formatter formatter = new DefaultFormatter(widthProvider, 315);

        RowFactory factory = new RowFactory() {
            @Override
            public Row create(RowForwarder forward) {
                return new ForwardingRow(formatter, widthProvider, 20, 315, forward);
            }

            @Override
            public Row create(Object... columns) {
                return new DefaultRow(formatter, widthProvider, 20, 315, columns);
            }

            @Override
            public Row translated(boolean capitalize, Object... columns) {
                return new DictionaryRow(formatter, widthProvider, 20, 315, new NaughtyTranslate(), capitalize, columns);
            }
        };

        DefaultTable table = new FormattedTable(widthProvider, factory, new DummyPaginator(), "Test {3} - page {0}/{1} - {2} entries", 2, "");

        table.addRow("fuck", "this");
        table.addRow("really", "hard").setAlignment(Align.RIGHT, Align.RIGHT);

        table.addRow("fuck", "this");

        table.addRow(factory.create(new TestRow()));

        System.out.println(table.render("test", 1));
    }

    private static final class TestRow implements RowForwarder {

        private final int[] values = {new Random().nextInt(), new Random().nextInt()};

        @Override
        public int getColumns() {
            return values.length;
        }

        @Override
        public String getColumn(int column) {
            return Integer.toString(values[column]);
        }
    }

}
