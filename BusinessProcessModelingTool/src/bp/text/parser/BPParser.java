package bp.text.parser;

import java.util.List;

public class BPParser {

    private final Lexer lexer;
    private String error;

    public BPParser() {
        this.lexer = new Lexer();
    }

    public void parse(final String input) {
        final List<Token> tokens = this.lexer.buildTokens(input);
        final Token invalidToken = findInvalidToken(tokens);
        if (invalidToken != null) {
            // there is some invalid token, meaning that syntax is invalid
            this.error = "Invalid syntax";
            return;
        }

    }

    /**
     * Returns <code>null</code> if <code>tokens</code> is null or if there are no invalid tokens in list.
     * 
     * @param tokens
     * @return
     */
    private Token findInvalidToken(final List<Token> tokens) {
        if (tokens == null) {
            return null;
        }
        for (final Token t : tokens) {
            if (t.getTokenType() == TokenType.UNKNOWN) {
                return t;
            }
        }
        return null;
    }
}
