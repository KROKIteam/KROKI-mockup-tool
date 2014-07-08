package bp.text.parser;

public enum TokenType {

    UNKNOWN,
    KEYWORD,
    IDENTIFIER,
    WHITESPACE,
    COMMENT,
    LINE_COMMENT,
    NUMBER,
    STRING,
    OPENED_CURVY_BRACKET,
    CLOSED_CURVY_BRACKET,
    ASSIGNMENT;
}
