package ilapin.basic.loxbasicparser;

public class Token {

    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    Token(final TokenType type, final String lexeme, final Object literal, final int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
