package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.operands.Operand;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.values.Value;
import interpreter.model.values.operationinterfaces.Comparable;
import interpreter.model.values.operationinterfaces.Logical;
import interpreter.model.values.operationinterfaces.Testable;

import static interpreter.model.operands.Operand.AND;
import static interpreter.model.operands.Operand.OR;

public class RelationalExpression implements Expression {
    Expression firstExpression, secondExpression;
    Operand operand;

    public RelationalExpression(Expression firstExpression, Expression secondExpression, Operand operand) throws ExpressionException {
        super();
        if (!operand.relational()) {
            throw new ExpressionException("Operand is not fit for a relational expression");
        }
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operand = operand;

    }

    public RelationalExpression(Operand operand, Expression firstExpression, Expression secondExpression) throws ExpressionException {
        super();
        if (!operand.relational()) {
            throw new ExpressionException("Operand is not fit for a relational expression");
        }
        this.operand = operand;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;

    }

    @SuppressWarnings({"rawtypes"})
    @Override
    public Value evaluate(ProgramState state) throws ExpressionException, ValueException, HeapException, SymbolTableException {
        Value firstValue = firstExpression.evaluate(state);
        if (firstValue instanceof Comparable cast) {
            Value secondValue = secondExpression.evaluate(state);
            return switch (operand) {
                case EQUAL -> cast.equal(secondValue);
                case NOT_EQUAL -> cast.notEqual(secondValue);
                case GREATER -> cast.greater(secondValue);
                case LOWER -> cast.lower(secondValue);
                case GREATER_OR_EQUAL -> cast.greaterOrEqual(secondValue);
                case LOWER_OR_EQUAL -> cast.lowerOrEqual(secondValue);
                default -> throw new ExpressionException("Operand provided is not suitable for a logical expression");
            };
        } else {
            throw new ExpressionException("First expression does not evaluate to a value of type suitable for the provided operand -- %s, operand %s".
                    formatted(firstExpression.toString(), operand.toString()));
        }
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type firstType = firstExpression.typecheck(environment),
                secondType = secondExpression.typecheck(environment);
        if (!firstType.equals(secondType))
            throw new TypecheckException("Mismatched types -- %s, %s".formatted(firstType, secondType));
        switch (operand){
            case EQUAL,NOT_EQUAL ->{
                if (!(firstType.getDefault() instanceof Testable)){
                    throw new TypecheckException("Expression does not evaluate to a testable type -- %s".formatted(firstType));
                }
            }
            case GREATER, GREATER_OR_EQUAL, LOWER, LOWER_OR_EQUAL ->{
                if (!(firstType.getDefault() instanceof Comparable)){
                    throw new TypecheckException("Expression does not evaluate to a comparable type -- %s".formatted(firstType));
                }
            }
            default -> throw new TypecheckException("Unaccepted operand type for a relational expression -- %s".formatted(operand));
        }
        return firstType;
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new RelationalExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }

    public String toString() {
        return "(" + firstExpression.toString() + " " + operand.toString() + " " + secondExpression.toString() + ")";
    }
}
