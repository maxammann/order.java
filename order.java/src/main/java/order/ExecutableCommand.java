package order;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ListeningExecutorService;
import order.sender.Sender;
import order.token.NamedToken;
import order.token.Token;
import order.token.TokenResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link ExecutableCommand} is basically a single command.
 */
public class ExecutableCommand<S extends Sender> extends Command<S> {

    private final List<Argument> arguments = new ArrayList<Argument>();


    public ExecutableCommand(String name, String identifier, String description,
                             Executor<S> executor, Executor<S> helpExecutor,
                             ListeningExecutorService service,
                             Class<? extends Sender> senderClass) {
        this(name, identifier, description, null, executor, helpExecutor, service, senderClass);
    }

    public ExecutableCommand(String name, String identifier, String description, String permission,
                             Executor<S> executor, Executor<S> helpExecutor,
                             ListeningExecutorService service,
                             Class<? extends Sender> senderClass) {
        super(name, identifier, description, permission, executor, helpExecutor, service, senderClass);
    }

    public List<Argument> getArguments() {
        return Collections.unmodifiableList(arguments);
    }


    @Override
    public int getArgumentsAmount() {
        return arguments.size();
    }


    public void addArgument(Argument argument) {
        this.arguments.add(argument);
    }

    private boolean isValid(TokenResult result) {
        return getArgumentsAmount() == result.getArgumentAmount();
    }

    private boolean convertArgument(TokenResult result) {
        return result.getArgumentAmount() == 1 && getArgumentsAmount() == 0 && getOptions().size() == 1;
    }

    @Override
    public CommandContext<S> createContext(final S sender, final TokenResult result) {
        boolean convert = convertArgument(result);

        if (!(isValid(result) || convert)) {
            return new CommandContext<S>(sender, getHelpExecutor(), this);
        }

        //beautify
        //Converts an argument to an option if there's only 1 argument specified and only 1 option available
        if (convert) {
            Argument option = Iterables.getOnlyElement(getOptions().values());
            Token argument = result.getArgument(0);
            result.addOption(new NamedToken(option.getInternalName(), argument.getValue()));
        }

        return new CommandContext<S>(sender, getExecutor(), this);
    }

    @Override
    public CommandContext<S> parse(CommandContext<S> ctx, TokenResult result) throws ParsingException {
        if (isValid(result)) {
            for (int i = 0, size = arguments.size(); i < size; i++) {
                Argument<?> argument = arguments.get(i);

                Object parsed = argument.parse(result.getArgument(i).getValue(), ctx);

                if (parsed == null) {
                    continue;
                }

                ctx.put(argument.getInternalName(), parsed);
            }
        }

        return super.parse(ctx, result);
    }

    @Override
    public String toString() {
        return "ExecutableCommand{" +
                "identifier='" + getIdentifier() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", executor=" + getExecutor() +
                ", arguments=" + arguments +
                ", options=" + getOptions() +
                ", helpExecutor=" + getHelpExecutor() +
                '}';
    }
}
