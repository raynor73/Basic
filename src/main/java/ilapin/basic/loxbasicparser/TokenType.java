package ilapin.basic.loxbasicparser;

public enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN,
    COMMA, DOT, MINUS, PLUS, SLASH, SEMICOLON, STAR, NUMBER_SIGN,

    // One or two character tokens.
    NOT_EQUAL,
    EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    /*AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,*/
    LET, PRINT, END, OR, AND, MOD, NOT,

    // Functions
    LEN,

    EOF
}
