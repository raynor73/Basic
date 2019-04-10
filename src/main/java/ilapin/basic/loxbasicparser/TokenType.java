package ilapin.basic.loxbasicparser;

public enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, STAR,

    // One or two character tokens.
    EQUAL,
    GREATER,
    LESS,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    /*AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,*/
    LET, PRINT, END,

    // Functions
    LEN,

    EOF
}
