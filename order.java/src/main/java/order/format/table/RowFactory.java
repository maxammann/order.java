package order.format.table;


import javax.inject.Named;

/**
 *
 */
public interface RowFactory {

    @Named("forward")
    Row create(RowForwarder forward);

    @Named("default")
    Row create(Object... columns);

    @Named("dictionary")
    Row translated(boolean capitalize, Object... columns);
}
