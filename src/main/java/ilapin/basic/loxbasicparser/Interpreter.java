package ilapin.basic.loxbasicparser;

import ilapin.basic.Main;

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
                checkNumberOperands(expr.operator, left, right);
                return (int) left > (int) right;
                
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (int) left >= (int) right;
                
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (int) left < (int) right;
                
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (int) left <= (int) right;
            
            case MOD:
                checkNumberOperands(expr.operator, left, right);
                return (int) left % (int) right;

            case PLUS:
                checkNumberOperands(expr.operator, left, right);
                return (int) left + (int) right;

            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (int) left - (int) right;

            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (int) left / (int) right;

            case STAR:
                checkNumberOperands(expr.operator, left, right);
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
                checkNumberOperand(expr.operator, right);
                return -(int)right;
        }

        // Unreachable.
        return null;
    }

    public void interpret(final Expr expression) {
        try {
            final Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (final RuntimeError error) {
            Main.runtimeError(error);
        }
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

    private void checkNumberOperand(final Token operator, final Object operand) {
        if (operand instanceof Integer) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(final Token operator, final Object left, final Object right) {
        if (left instanceof Integer && right instanceof Integer) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private String stringify(final Object object) {
        if (object == null) return "null";
        return object.toString();
    }
}
