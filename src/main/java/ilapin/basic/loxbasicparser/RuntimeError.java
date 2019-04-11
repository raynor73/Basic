package ilapin.basic.loxbasicparser;

public class RuntimeError extends RuntimeException {

    public final Token token;

    RuntimeError(final Token token, final String message) {
        super(message);
        this.token = token;
    }
}