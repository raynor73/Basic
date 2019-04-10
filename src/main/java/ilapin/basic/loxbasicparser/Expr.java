package ilapin.basic.loxbasicparser;

public abstract class Expr {

    //abstract <R> R accept(Visitor<R> visitor);

    public static class Binary extends Expr {

        public final Expr left;
        public final Token operator;
        public final Expr right;

        public Binary(final Expr left, final Token operator, final Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }

    public static class Grouping extends Expr {

        public final Expr expression;

        public Grouping(final Expr expression) {
            this.expression = expression;
        }
    }

    public static class Literal extends Expr {

        public final Object value;

        public Literal(final Object value) {
            this.value = value;
        }
    }

    public static class Unari extends Expr {

        public final Token operator;
        public final Expr right;

        public Unari(final Token operator, final Expr right) {
            this.operator = operator;
            this.right = right;
        }
    }
}
