package order.reflect.instance;

import com.google.common.util.concurrent.ListeningExecutorService;
import order.*;
import order.parser.ArgumentParser;
import order.reflect.Entry;
import order.reflect.Meta;
import order.reflect.Option;
import order.reflect.Permission;
import order.reflect.instance.factory.InstanceFactory;
import order.sender.Sender;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.Map;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPrivate;

/**
 * Represents a CommandFactory
 */
public class CommandAnalyser<S extends Sender> {

    public static final String FAILED = "Annotated field \"%s\" in \"%s\" mustn't be private or final!";

    private final InstanceFactory instanceFactory;
    private final Translate dictionary;

    private final Executor<S> helpExecutor;
    private final Executor<S> groupHelpExecutor;

    private final Map<Class<?>, ArgumentParser<?>> parsers;

    private final CommandPipeline<S> pipeline;
    private final CommandPipeline<S> helpPipeline;
    private final ListeningExecutorService syncService;
    private final ListeningExecutorService asyncService;


    @Inject
    public CommandAnalyser(@Named("help-executor") Executor<S> helpExecutor,
                           @Named("group-help-executor") Executor<S> groupHelpExecutor,
                           @Named("parsers") Map<Class<?>, ArgumentParser<?>> parsers,
                           InstanceFactory instanceFactory,
                           CommandPipeline<S> pipeline,
                           @Named("help-pipeline") CommandPipeline<S> helpPipeline,
                           @Named("sync-executor") ListeningExecutorService syncService,
                           @Named("async-executor") ListeningExecutorService asyncService,
                           Translate dictionary) {

        this.groupHelpExecutor = groupHelpExecutor;
        this.pipeline = pipeline;
        this.helpPipeline = helpPipeline;
        this.syncService = syncService;
        this.asyncService = asyncService;
        this.helpExecutor = helpExecutor;
        this.parsers = parsers;
        this.instanceFactory = instanceFactory;
        this.dictionary = dictionary;
    }

    public Command<S> analyse(Class<?> clazz) {
        GroupCommand<S> groupCommand = analyseGroup(clazz);

        if (groupCommand != null) {
            return groupCommand;
        }

        return analyseExecutableCommand((Class<? extends Executor<S>>) clazz);
    }

    private ExecutableCommand<S> analyseExecutableCommand(Class<? extends Executor<S>> clazz) {
        order.reflect.Command commandAnnotation = clazz
                .getAnnotation(order.reflect.Command.class);

        String name = commandAnnotation.name();

        if (name.equals(order.reflect.Command.NO_VALUE)) {
            name = clazz.getSimpleName().replace("Command", "");
        }

        //beautify
        String identifier = commandAnnotation.identifier();
        String description = commandAnnotation.description();

        if (description.equals(order.reflect.Command.NO_VALUE)) {
            description = identifier + ".description";
        }

        ExecutableCommand<S> command = new ExecutableCommand<S>(
                dictionary.getTranslation(name),
                dictionary.getTranslation(identifier),
                dictionary.getTranslation(description),
                pipeline.build(new InstanceExecutor<S>(instanceFactory, clazz)),
                helpPipeline.build(helpExecutor),
                commandAnnotation.async() ? asyncService : syncService,
                getSender(clazz)
        );

        addArguments(command, clazz);
        addOptions(command, clazz);
        addMetadata(command, clazz);
        setPermission(command, clazz);
        return command;
    }

    private void setPermission(Command<S> command, Class<?> clazz) {
        Permission annotation = clazz.getAnnotation(Permission.class);
        if (annotation == null) {
            return;
        }

        command.setPermission(annotation.value());
    }

    private void addMetadata(Command<S> command, Class<?> clazz) {
        Meta annotation = clazz.getAnnotation(Meta.class);
        if (annotation == null) {
            return;
        }

        Entry[] entries = annotation.value();

        for (Entry entry : entries) {
            String key = entry.key();
            String value = entry.value();

            command.put(key, value);
        }
    }

    private ArgumentParser<Object> findParser(Field field, Class clazz) {
        if (isPrivate(field.getModifiers()) || isFinal(field.getModifiers())) {
            throw new IllegalStateException(String.format(FAILED, field.getName(), clazz));
        }

        ArgumentParser<?> obj = parsers.get(field.getType());

        if (obj == null) {
            throw new IllegalStateException("Parser for class type " + field.getType() + " not found!");
        }

        return (ArgumentParser<Object>) obj;
    }

    private void addArguments(ExecutableCommand<S> command, Class<? extends Executor<S>> clazz) {

        for (Field field : clazz.getDeclaredFields()) {
            order.reflect.Argument argument = field
                    .getAnnotation(order.reflect.Argument.class);


            if (argument == null) {
                continue;
            }

            ArgumentParser<?> obj = findParser(field, clazz);

            if (obj == null) {
                throw new IllegalStateException("Parser for class type " + field.getType() + " not found!");
            }

            ArgumentParser<Object> parser = (ArgumentParser<Object>) obj;

            //beautify
            String name = argument.name();
            String description = argument.description();

            if (description.equals(order.reflect.Argument.NO_VALUE)) {
                description = name + ".description";
            }

            command.addArgument(new Argument<Object>(
                            field.getName(),
                            dictionary.getTranslation(name),
                            dictionary.getTranslation(description),
                            false,
                            parser)
            );
        }
    }

    private void addOptions(Command<S> command, Class<?> clazz) {

        for (Field field : clazz.getDeclaredFields()) {
            Option option = field.getAnnotation(Option.class);

            if (option == null) {
                continue;
            }

            ArgumentParser<Object> parser = findParser(field, clazz);

            //beautify
            String name = option.name();
            String description = option.description();

            if (description.equals(order.reflect.Option.NO_VALUE)) {
                description = name + ".description";
            }

            command.addOption(
                    new Argument<Object>(field.getName(),
                            dictionary.getTranslation(name),
                            dictionary.getTranslation(description),
                            true,
                            parser)
            );
        }
    }


    private GroupCommand<S> analyseGroup(Class<?> clazz) {
        order.reflect.Command command = clazz
                .getAnnotation(order.reflect.Command.class);

        Children children = clazz.getAnnotation(Children.class);

        if (children == null) {
            return null;
        }

        Class<? extends Executor<S>> helpClass = (Class<? extends Executor<S>>) groupHelpExecutor.getClass();

        Executor<S> executor;
        Executor<S> helpExecutor = helpPipeline.build(new InstanceExecutor<S>(instanceFactory, helpClass));


        if (Executor.class.isAssignableFrom(clazz)) {
            executor = pipeline.build(new InstanceExecutor<S>(
                            instanceFactory, (Class<? extends Executor<S>>) clazz)
            );
        } else {
            executor = helpExecutor;
        }

        String name = command.name();

        if (name.equals(order.reflect.Command.NO_VALUE)) {
            name = clazz.getSimpleName().replace("Command", "");
        }

        String identifier = command.identifier();
        String description = command.description();

        if (description.equals(order.reflect.Command.NO_VALUE)) {
            description = identifier + ".description";
        }

        String translated = dictionary.getTranslation(identifier);

        GroupCommand<S> groupCommand = new GroupCommand<S>(
                dictionary.getTranslation(name),
                translated,
                dictionary.getTranslation(description),
                executor,
                helpExecutor,
                syncService,
                getSender(clazz));

        for (Class<?> child : children.value()) {
            groupCommand.addChild(analyse(child));
        }

        analyseOptions(groupCommand, clazz);
        analyseOptions(groupCommand, groupHelpExecutor.getClass());
        addMetadata(groupCommand, clazz);
        setPermission(groupCommand, clazz);

        return groupCommand;
    }

    public void analyseOptions(GroupCommand<S> command, Class<?> executor) {
        addOptions(command, executor);
    }

    @NotNull
    private Class<? extends Sender> getSender(Class<?> clazz) {
        order.reflect.Sender senderAnnotation = clazz
                .getAnnotation(order.reflect.Sender.class);
        Class<? extends Sender> sender = Command.DEFAULT_SENDER;

        if (senderAnnotation != null) {
            sender = senderAnnotation.value();
        }

        return sender;
    }

}
