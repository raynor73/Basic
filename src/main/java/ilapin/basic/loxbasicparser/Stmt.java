package ilapin.basic.loxbasicparser;

public abstract class Stmt {

    interface Visitor<R> {
        R visitPrintStmt(Print stmt);
    }

    public static class Print extends Stmt {

        public final Expr expression;

        Print(final Expr expression) {
            this.expression = expression;
        }

        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    abstract <R> R accept(final Visitor<R> visitor);
}
