package ilapin.basic.loxbasicparser;

public class Interpreter implements Expr.Visitor<Object> {

    @Override
    public Object visitBinaryExpr(final Expr.Binary expr) {
        final Object left = evaluate(expr.left);
        final Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case NOT_EQUAL:
                return !isEqual(left, right);

            case EQUAL:
                return isEqual(left, right);

            case GREATER:
                return (int) left > (int) right;
                
            case GREATER_EQUAL:
                return (int) left >= (int) right;
                
            case LESS:
                return (int) left < (int) right;
                
            case LESS_EQUAL:
                return (int) left <= (int) right;
            
            case MOD:
                return (int) left % (int) right;

            case PLUS:
                return (int) left + (int) right;

            case MINUS:
                return (int) left - (int) right;

            case SLASH:
                return (int) left / (int) right;

            case STAR:
                return (int) left * (int) right;
        }

        // Unreachable.
        return null;
    }

    @Override
    public Object visitGroupingExpr(final Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(final Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(final Expr.Unary expr) {
        final Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case NOT:
                return !isTruthy(right);
            case MINUS:
                return -(int)right;
        }

        // Unreachable.
        return null;
    }

    private Object evaluate(final Expr expr) {
        return expr.accept(this);
    }

    private boolean isEqual(final Object a, final Object b) {
        if (a == null || b == null) throw new NullPointerException();
        return a.equals(b);
    }

    private boolean isTruthy(final Object object) {
        if (object == null) throw new NullPointerException();
        if (object instanceof Integer) return (int) object != 0;
        throw new RuntimeException("Unexpected type: " + object.getClass().getSimpleName());
    }
}
