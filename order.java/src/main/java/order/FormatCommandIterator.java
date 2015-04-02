package order;

import order.sender.Sender;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public abstract class FormatCommandIterator<S extends Sender> implements CommandIterator<S> {

    private final String prefix;

    private final String descriptionPrefix;
    private final boolean description;
    private final String groupSuffix;

    public FormatCommandIterator(String prefix, String descriptionPrefix, String groupSuffix) {
        this(prefix, descriptionPrefix, true, groupSuffix);
    }

    public FormatCommandIterator(String prefix, String descriptionPrefix, boolean description, String groupSuffix) {
        this.prefix = prefix;
        this.descriptionPrefix = descriptionPrefix;
        this.description = description;
        this.groupSuffix = groupSuffix;
    }

    @Override
    public void iterate(Command<S> command, Deque<Command<S>> stack) {
        StringBuilder builder = new StringBuilder();

        if (command instanceof ExecutableCommand) {
            ExecutableCommand<S> executableCommand = (ExecutableCommand<S>) command;

            append(builder, executableCommand, stack);
            appendDesc(builder, command);
        } else {
            append(builder, command, stack);
            appendDesc(builder, command);
        }

        iterate(command, builder.toString());
    }

    public abstract void iterate(Command<S> command, String format);

    private StringBuilder append(StringBuilder builder, Command<S> command, Deque<Command<S>> pre) {
        builder.append(prefix);

        for (Iterator<Command<S>> it = pre.descendingIterator(); it.hasNext(); ) {
            String identifier = it.next().getIdentifier();

            if (identifier == null || identifier.isEmpty()) {
                continue;
            }

            builder.append(identifier).append(' ');
        }

        builder.append(command.getIdentifier());

        if (command instanceof GroupCommand) {
            builder.append(groupSuffix);
        }
        return builder;
    }

    private StringBuilder append(StringBuilder builder, ExecutableCommand<S> command, Deque<Command<S>> pre) {
        append(builder, (Command<S>) command, pre);

        List<Argument> arguments = command.getArguments();

        for (Argument argument : arguments) {
            builder.append(" [").append(argument.getName()).append(']');
        }

        for (Argument option : command.getOptions().values()) {
            builder.append(" [--").append(option.getName()).append(']');
        }

        return builder;
    }

    private StringBuilder appendDesc(StringBuilder builder, Command<?> command) {
        if (description) {
            builder.append(descriptionPrefix).append(command.getDescription());
        }
        return builder;
    }
}
