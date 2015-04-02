package order;

import order.sender.Sender;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This context holds the execution information while a command is being executed. It contains information about
 * the {@link order.sender.Sender}, {@link Command} and the
 * arguments which will be used to execute the command.
 */
public class CommandContext<S extends Sender> {

    private final S sender;

    private final Command<S> command;
    private final Executor<S> executor;
    private final Map<String, Object> argumentValues;

    private boolean cancelled;

    public CommandContext(S sender, Executor<S> executor, Command<S> command) {
        this(sender, command, executor, new HashMap<String, Object>());
    }

    public CommandContext(S sender, Command<S> command, Executor<S> executor, Map<String, Object> argumentValues) {
        this.sender = sender;
        this.command = command;
        this.executor = executor;
        this.argumentValues = argumentValues;
    }

    public Object put(String key, Object value) {
        return argumentValues.put(key, value);
    }

    @Nullable
    public <T> T get(String argument) {
        Object value = argumentValues.get(argument);

        if (value == null) {
            return null;
        }

        return (T) value;
    }

    public int getPage() {
        Integer page = get("page");

        if (page == null) {
            return 0;
        }

        return page;
    }

    public int getArguments() {
        return argumentValues.size();
    }

    public Map<String, Object> getArgumentValues() {
        return Collections.unmodifiableMap(argumentValues);
    }

    public S getSender() {
        return sender;
    }

    public void execute() throws ExecuteException {
        executor.execute(this, sender);
    }

    public Command<S> getCommand() {
        return command;
    }

    public String getName() {
        return getCommand().getName();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
