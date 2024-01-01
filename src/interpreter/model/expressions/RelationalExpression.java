package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.operands.Operand;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.BoolType;
import interpreter.model.type.Type;
import interpreter.model.values.Value;
import interpreter.model.values.operationinterfaces.Comparable;
import interpreter.model.values.operationinterfaces.Testable;

public class RelationalExpression implements Expression {
    final Expression firstExpression;
    final Expression secondExpression;
    final Operand operand;

    public RelationalExpression(Expression firstExpression, Expression secondExpression, Operand operand) {
        super();
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operand = operand;

    }

    public RelationalExpression(Operand operand, Expression firstExpression, Expression secondExpression) {
        super();
        this.operand = operand;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;

    }

    @SuppressWarnings({"rawtypes"})
    @Override
    public Value evaluate(ProgramState state) throws ExpressionException, ValueException, HeapException, SymbolTableException {
        if (operand == Operand.EQUAL || operand == Operand.NOT_EQUAL) {
            Testable firstValue = (Testable) firstExpression.evaluate(state);
            Value secondValue = secondExpression.evaluate(state);
            return switch (operand) {
                case EQUAL -> firstValue.equal(secondValue);
                case NOT_EQUAL -> firstValue.notEqual(secondValue);
                default -> null;
            };
        }
        Comparable firstValue = (Comparable) firstExpression.evaluate(state);
        Value secondValue = secondExpression.evaluate(state);
        return switch (operand) {
            case GREATER -> firstValue.greater(secondValue);
            case LOWER -> firstValue.lower(secondValue);
            case GREATER_OR_EQUAL -> firstValue.greaterOrEqual(secondValue);
            case LOWER_OR_EQUAL -> firstValue.lowerOrEqual(secondValue);
            default -> null;
        };
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type firstType = firstExpression.typecheck(environment),
                secondType = secondExpression.typecheck(environment);
        if (firstType != secondType)
            throw new TypecheckException("Mismatched types -- %s, %s".formatted(firstType, secondType));
        switch (operand) {
            case EQUAL, NOT_EQUAL -> {
                if (!(firstType.getDefault() instanceof Testable)) {
                    throw new TypecheckException("Expression does not evaluate to a testable type -- %s".formatted(firstType));
                }
            }
            case GREATER, GREATER_OR_EQUAL, LOWER, LOWER_OR_EQUAL -> {
                if (!(firstType.getDefault() instanceof Comparable)) {
                    throw new TypecheckException("Expression does not evaluate to a comparable type -- %s".formatted(firstType));
                }
            }
            default ->
                    throw new TypecheckException("Unaccepted operand type for a relational expression -- %s".formatted(operand));
        }
        return BoolType.get();
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new RelationalExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }

    public String toString() {
        return "(" + firstExpression.toString() + " " + operand.toString() + " " + secondExpression.toString() + ")";
    }
}
