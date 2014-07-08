package bp.text.parser;

public class TokenFactory {

    public final Token createKeywordToken(final String keyWord) {
        return new Token(keyWord, TokenType.KEYWORD);
    }

    public final Token createIdentifierToken(final String id) {
        return new Token(id, TokenType.IDENTIFIER);
    }

    public final Token createNumberToken(final String number) {
        return new Token(number, TokenType.NUMBER);
    }

    public final Token createStringToken(final String string) {
        return new Token(string, TokenType.STRING);
    }

    public final Token createWhitespaceToken(final String ws) {
        return new Token(ws, TokenType.WHITESPACE);
    }

    public final Token createLineCommentToken(final String comment) {
        return new Token(comment, TokenType.LINE_COMMENT);
    }

    public final Token createCommentToken(final String comment) {
        return new Token(comment, TokenType.COMMENT);
    }

    public final Token createOpenedCurvyBracketToken() {
        return new Token("{", TokenType.OPENED_CURVY_BRACKET);
    }

    public final Token createClosedCurvyBracketToken() {
        return new Token("}", TokenType.CLOSED_CURVY_BRACKET);
    }

    public final Token createAssignmentToken() {
        return new Token(":", TokenType.ASSIGNMENT);
    }

    public final Token createUnknownToken(final String content) {
        return new Token(content, TokenType.UNKNOWN);
    }
}
