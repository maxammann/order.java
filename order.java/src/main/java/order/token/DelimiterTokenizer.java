package order.token;


import javax.inject.Inject;

/**
 * Represents a DefaultTokenizer
 */
public class DelimiterTokenizer implements Tokenizer {

    private final Delimiter delimiter;

    @Inject
    public DelimiterTokenizer(Delimiter delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public TokenResult tokenize(String identifier, String[] args) {
        StringBuilder builder = new StringBuilder();

        builder.append(identifier).append(' ');

        for (String argument : args) {
            builder.append(argument).append(' ');
        }

        if (builder.length() != 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return tokenize(builder.toString());
    }

    @Override
    public TokenResult tokenize(String input) {
        String[] delimited = delimiter.delimit(input);

        if (delimited.length == 0) {
            return new TokenResult();
        }

        return tokenize(delimited);
    }

    public TokenResult tokenize(String[] delimited) {

        TokenResult result = new TokenResult(null);

        for (int i = 0, length = delimited.length; i < length; i++) {
            String part = delimited[i];

            if (part.startsWith("--") && delimited.length > i + 1) {
                result.addOption(new NamedToken(part.substring(2), delimited[++i]));
                continue;
            }

            // If last argument -> try page
            if (i == length - 1) {
                try {
                    Integer.parseInt(part);
                    result.addOption(new NamedToken("page", part));
                } catch (NumberFormatException ignored) {
                }
            }

            result.addArgument(new Token(part));
        }


        return result;
    }
}
