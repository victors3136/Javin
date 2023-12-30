package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.operands.Operand;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.BoolType;
import interpreter.model.type.Type;
import interpreter.model.values.Value;
import interpreter.model.values.operationinterfaces.Logical;


public class LogicExpression implements Expression {
    Expression firstExpression, secondExpression;
    Operand operand;

    public LogicExpression(Expression firstExpression, Expression secondExpression, Operand operand) {
        super();
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operand = operand;
    }

    public LogicExpression(Operand operand, Expression firstExpression, Expression secondExpression) {
        super();
        this.operand = operand;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;

    }

    @Override
    public Value evaluate(ProgramState state) throws ExpressionException, ValueException, HeapException, SymbolTableException {
        Logical firstValue = (Logical) firstExpression.evaluate(state);
        Value secondValue = secondExpression.evaluate(state);
        return switch (operand) {
            case OR -> firstValue.or(secondValue);
            case AND -> firstValue.and(secondValue);
            default -> null;
        };
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type firstType = firstExpression.typecheck(environment),
                secondType = secondExpression.typecheck(environment);
        if (firstType != secondType)
            throw new TypecheckException("Mismatched types -- %s, %s".formatted(firstType, secondType));
        if (!operand.logical()) {
            throw new TypecheckException("Unaccepted operand type for arithmetic expression -- %s".formatted(operand));
        }
        if (!(firstType.getDefault() instanceof Logical)) {
            throw new TypecheckException("Expression does not evaluate to a logical type -- %s".formatted(firstType));
        }
        return BoolType.get();
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new LogicExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }

    public String toString() {
        return "(" + firstExpression.toString() + " " + operand.toString() + " " + secondExpression.toString() + ")";
    }
}
