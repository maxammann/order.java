package order;

import com.google.common.util.concurrent.ListeningExecutorService;
import order.sender.Sender;
import order.token.NamedToken;
import order.token.TokenResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Command} is an abstract construct.
 * The default implementations are {@link ExecutableCommand}
 * and {@link GroupCommand}.
 */
public abstract class Command<S extends Sender> {

    public final static Class<? extends Sender> DEFAULT_SENDER = Sender.class;

    private final String name;
    private final String identifier;
    private final String description;

    private GroupCommand<S> parent;

    private String permission;
    private final Executor<S> executor;
    private final Executor<S> helpExecutor;

    private final HashMap<String, Object> metadata = new HashMap<String, Object>();

    private final HashMap<String, Argument> options = new HashMap<String, Argument>();

    private final Class<? extends Sender> senderClass;

    private final ListeningExecutorService service;


    public Command(String name, String identifier, String description, Executor<S> executor, ListeningExecutorService service, Class<? extends Sender> senderClass, Executor<S> helpExecutor) {
        this(name, identifier, description, null, executor, helpExecutor, service, senderClass);
    }

    public Command(String name, String identifier, String description, String permission, Executor<S> executor, Executor<S> helpExecutor, ListeningExecutorService service, Class<? extends Sender> senderClass) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.permission = permission;
        this.executor = executor;
        this.helpExecutor = helpExecutor;
        this.service = service;
        this.senderClass = senderClass;
    }

    /**
     * @return The name of this command
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of this command
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The identifier used to lookup this command
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return The amount of required arguments
     */
    public abstract int getArgumentsAmount();

    /**
     * @return The executor which is used to execute this command
     */
    public Executor<S> getExecutor() {
        return executor;
    }

    /**
     * @return The executor which provides help with this command
     */
    public Executor<S> getHelpExecutor() {
        return helpExecutor;
    }

    /**
     * Tries to create a new {@link CommandContext} by returning an {@link ExecutableCommand} hopefully.
     * If this can also return a {@link GroupCommand} and an executor which provides help ({@link #getHelpExecutor()})
     *
     * @param sender The sender
     * @param result The token result from parsing
     * @return The found context
     */
    public abstract CommandContext<S> createContext(S sender, TokenResult result);

    /**
     * Parses a {@link order.token.TokenResult} and tries to add arguments/options to the context.
     *
     * @param ctx    The context
     * @param result The result
     * @return The final context
     * @throws ParsingException
     */
    public CommandContext<S> parse(CommandContext<S> ctx, TokenResult result) throws ParsingException {
        for (NamedToken token : result.getOptions()) {
            Argument<?> option = getOptions().get(token.getName());
            if (option == null) {
                continue;
//                throw new ParsingException("Option \"%s\" not found!", ctx, token.getName());
            }
            Object parsed = option.parse(token.getValue(), ctx);

            if (parsed == null) {
                continue;
            }

            ctx.put(token.getName(), parsed);
        }

        return ctx;
    }

    /**
     * @return The service in which the command will be executed in
     */
    public ListeningExecutorService getService() {
        return service;
    }

    @Override
    public String toString() {
        return "Command{" +
                "identifier='" + identifier + '\'' +
                ", description='" + description + '\'' +
                ", executor=" + executor +
                '}';
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Class<? extends Sender> getSenderClass() {
        return senderClass;
    }

    public Map<String, Argument> getOptions() {
        return Collections.unmodifiableMap(options);
    }


    public void addOption(Argument option) {
        // Get by name from translated set
        this.options.put(option.getInternalName(), option);
    }


    public Object put(String key, Object value) {
        return metadata.put(key, value);
    }

    public void load(Map<String, Object> metadata) {
        this.metadata.putAll(metadata);
    }

    @Nullable
    public <T> T get(String key) {
        Object value = metadata.get(key);

        if (value == null) {
            return null;
        }

        return (T) value;
    }

    public GroupCommand<S> getParent() {
        return parent;
    }

    public void setParent(GroupCommand<S> parent) {
        this.parent = parent;
    }
}
