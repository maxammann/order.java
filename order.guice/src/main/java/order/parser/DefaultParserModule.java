package order.parser;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import order.sender.Sender;

import java.io.File;

/**
 * Represents a ParserModule
 */
public class DefaultParserModule extends AbstractModule {

    @Override
    protected void configure() {
        MapBinder<Class<?>, ArgumentParser<?>> parsers = MapBinder
                .newMapBinder(binder(), new TypeLiteral<Class<?>>() {}, new TypeLiteral<ArgumentParser<?>>() {}, Names
                        .named("parsers"));

        bind(new TypeLiteral<ArgumentParser<Number>>() {}).to(NumberParser.class);
        parsers.addBinding(Number.class).to(NumberParser.class);

        bind(new TypeLiteral<ArgumentParser<File>>() {}).to(FileParser.class);
        parsers.addBinding(File.class).to(FileParser.class);

        bind(new TypeLiteral<ArgumentParser<Boolean>>() {}).to(BooleanParser.class);
        parsers.addBinding(Boolean.class).to(BooleanParser.class);
        parsers.addBinding(boolean.class).to(BooleanParser.class);

        bind(new TypeLiteral<ArgumentParser<Double>>() {}).to(DoubleParser.class);
        parsers.addBinding(Double.class).to(DoubleParser.class);
        parsers.addBinding(double.class).to(DoubleParser.class);

        bind(new TypeLiteral<ArgumentParser<Integer>>() {}).to(IntegerParser.class);
        parsers.addBinding(Integer.class).to(IntegerParser.class);
        parsers.addBinding(int.class).to(IntegerParser.class);

        bind(new TypeLiteral<ArgumentParser<String>>() {}).to(StringParser.class);
        parsers.addBinding(String.class).to(StringParser.class);

        bind(new TypeLiteral<ArgumentParser<Sender>>() {}).to(TargetParser.class);
        parsers.addBinding(Sender.class).to(TargetParser.class);
    }
}
