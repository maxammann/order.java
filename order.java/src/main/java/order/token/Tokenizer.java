package order.token;

/**
 *
 */
public interface Tokenizer {

    TokenResult tokenize(String identifier, String[] args);

    TokenResult tokenize(String input);
}
