package order;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ListeningExecutorService;
import order.sender.Sender;
import order.token.TokenResult;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A {@link GroupCommand} contains multiple {@link Command}s.
 */
public class GroupCommand<S extends Sender> extends Command<S> implements CommandResolver<S>, Iterable<Command<S>> {

    //beautify over-think data structure
    private final Map<String, ArrayList<Command<S>>> children = new LinkedHashMap<String, ArrayList<Command<S>>>();

    private int maxArguments = 0;

    public GroupCommand(String name, String identifier, String description,
                        @Nullable Executor<S> executor, Executor<S> helpExecutor,
                        ListeningExecutorService service,
                        Class<? extends Sender> senderClass) {
        this(name, identifier, description, null, executor, helpExecutor, service, senderClass);
    }

    public GroupCommand(String name, String identifier, String description, String permission,
                        @Nullable Executor<S> executor, Executor<S> helpExecutor,
                        ListeningExecutorService service,
                        Class<? extends Sender> senderClass) {
        super(name, identifier, description, permission, executor, helpExecutor, service, senderClass);
    }

    @Override
    public int getArgumentsAmount() {
        return maxArguments;
    }

    @Override
    @Nullable
    public Executor<S> getExecutor() {
        return super.getExecutor();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public Iterable<Command<S>> getChildren() {
        return Iterables.concat(children.values());
    }

    public int size() {
        return children.size();
    }

    @Override
    public Command<S> find(String identifier, int arguments) {
        ArrayList<Command<S>> commands = children.get(identifier);

        if (commands == null) {
            return null;
        }

        // Find command with right argument amount
        int[] argumentAmounts = new int[commands.size()];

        for (int i = 0, size = commands.size(); i < size; i++) {
            argumentAmounts[i] = commands.get(i).getArgumentsAmount();
        }

        int search = Arrays.binarySearch(argumentAmounts, arguments);


        //beautify
        // If there's only one argument, check if the found command has only one option -> return it!
        if (arguments == 1) {
            search = Arrays.binarySearch(argumentAmounts, 0);
            if (search >= 0) {
                Command<S> command = commands.get(search);
                if (command.getArgumentsAmount() == 0 && command.getOptions().size() == 1) {
                    return command;
                }
            }
        }

        int index = search < 0 ? Math.abs(search) - 1 : search;

        return commands.get(Math.min(index, commands.size() - 1));
    }

    public void addChild(Command<S> command) {
        maxArguments += Math.max(maxArguments, command.getArgumentsAmount());

        String identifier = command.getIdentifier();

        ArrayList<Command<S>> commands = children.get(identifier);

        if (commands == null) {
            children.put(identifier, commands = new ArrayList<Command<S>>());
        }

        commands.add(command);
        command.setParent(this);
    }

    @Override
    public CommandContext<S> createContext(S sender, TokenResult result) {
        result.slice();

        String identifier = result.getIdentifier();

        if (identifier == null || identifier.equals("?")) {
            return new CommandContext<S>(sender, getHelpExecutor(), this);
        }

        Command<S> command = find(identifier, result.getArgumentAmount());

        if (result.isEmpty() || command == null) {
            return new CommandContext<S>(sender, getExecutor() != null ? getExecutor() : getHelpExecutor(), this);
        }

        return command.createContext(sender, result);
    }

    @Override
    public String toString() {
        return "GroupCommand{" +
                "identifier='" + getIdentifier() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", executor=" + getExecutor() +
                ", children=" + children +
                ", maxArguments=" + maxArguments +
                '}';
    }

    @Override
    public Iterator<Command<S>> iterator() {
        final ArrayList<Command<S>> commands = new ArrayList<Command<S>>();

        listChildrenRecursively(new CommandIterator<S>() {
            @Override
            public void iterate(Command<S> command, Deque<Command<S>> stack) {
                commands.add(command);
            }
        }, true);

        return commands.iterator();
    }

    public void iterate(CommandIterator<S> iterator, boolean recursive) {
        listChildrenRecursively(iterator, recursive);
    }

    private void listChildrenRecursively(CommandIterator<S> iterator, boolean recursive) {
        LinkedList<Command<S>> stack = new LinkedList<Command<S>>();

        for (GroupCommand<S> groupCommand = this; groupCommand != null; groupCommand = groupCommand.getParent()) {
            stack.addLast(groupCommand);
        }

        listChildrenRecursively(this, stack, iterator, recursive);
    }

    private void listChildrenRecursively(GroupCommand<S> command, LinkedList<Command<S>> stack, CommandIterator<S> iterator, boolean recursive) {

        for (Command<S> cmd : command.getChildren()) {
            if (recursive && cmd instanceof GroupCommand) {
                stack.push(cmd);
                listChildrenRecursively(((GroupCommand<S>) cmd), stack, iterator, true);
                stack.pop();
            }

            iterator.iterate(cmd, stack); //beautify immutable stack
        }
    }
}
