package order.token;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a Token
 */
public class TokenResult {

    private String identifier;

    private final Set<NamedToken> options = new HashSet<NamedToken>();
    private final List<Token> arguments = new ArrayList<Token>();

    public TokenResult() {
        this(null);
    }

    public TokenResult(String identifier) {
        this.identifier = identifier;
    }

    public Set<NamedToken> getOptions() {
        return options;
    }

    public List<Token> getArguments() {
        return arguments;
    }

    public int getArgumentAmount() {
        return arguments.size();
    }

    public Token getArgument(int index) {
        return arguments.get(index);
    }

    public TokenResult slice() {
        if (arguments.isEmpty()) {
            return this;
        }
        this.identifier = arguments.get(0).getValue();
        this.arguments.remove(0);
        return this;
    }

    public boolean isEmpty() {
        return identifier == null;
    }

    public void addArgument(Token token) {
        arguments.add(token);
    }

    public void addOption(NamedToken token) {
        options.add(token);
    }

    @Nullable
    public String getIdentifier() {
        return identifier;
    }

    public boolean hasArguments() {
        return arguments.size() > 0;
    }
}
