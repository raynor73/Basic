package ilapin.basic;

import ilapin.basic.loxbasicparser.*;
import ilapin.basic.vm.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private final BasicVirtualMachine vm =
            BasicVirtualMachineFactory.createVm(new StdoutStringPrinter());

    private static boolean hadError;

    public static void main(final String[] args) throws IOException {
        final Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(new Expr.Literal(45.67))
        );

        System.out.println(new AstPrinter().print(expression));

        /*if (args.length != 1) {
            System.out.println("Usage: basic [script]");
            System.exit(64);
        } else {
            runFile(args[0]);
        }*/
    }

    private static void runFile(final String path) throws IOException {
        final byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) {
            System.exit(65);
        }
    }

    private static void run(final String source) {
        final Scanner scanner = new Scanner(source);
        final List<Token> tokens = scanner.scanTokens();

        // For now, just print the tokens.
        for (final Token token : tokens) {
            System.out.println(token);
        }
    }

    public static void error(final int line, final String message) {
        report(line, "", message);
    }

    private static void report(final int line, final String where, final String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
