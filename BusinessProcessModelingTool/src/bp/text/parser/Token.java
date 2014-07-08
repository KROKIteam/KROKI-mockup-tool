package bp.text.parser;

public class Token {

    private final String content;
    private final TokenType tokenType;

    public Token(final String content, final TokenType type) {
        this.content = content;
        this.tokenType = type;
    }

    public String getContent() {
        return this.content;
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    @Override
    public String toString() {
        return String.format("[%s]: %s", getTokenType().toString(), getContent());
    }
}
