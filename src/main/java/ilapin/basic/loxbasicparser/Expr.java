package ilapin.basic.loxbasicparser;

public abstract class Expr {

    abstract <R> R accept(Visitor<R> visitor);

    public static class Binary extends Expr {

        public final Expr left;
        public final Token operator;
        public final Expr right;

        public Binary(final Expr left, final Token operator, final Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Grouping extends Expr {

        public final Expr expression;

        public Grouping(final Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    public static class Literal extends Expr {

        public final Object value;

        public Literal(final Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    public static class Unary extends Expr {

        public final Token operator;
        public final Expr right;

        public Unary(final Token operator, final Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public interface Visitor<R> {

        R visitBinaryExpr(final Binary expr);

        R visitGroupingExpr(final Grouping expr);

        R visitLiteralExpr(final Literal expr);

        R visitUnaryExpr(final Unary expr);
    }
}
