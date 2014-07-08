package bp.text.parser;

public enum LexerState {

    TEXT_STATE,
    NUMBER_STATE,
    STRING_STATE,
    LINE_COMMENT_STATE,
    COMMENT_STATE,
    WHITESPACE_STATE,
    UNKNOWN_STATE;
}
