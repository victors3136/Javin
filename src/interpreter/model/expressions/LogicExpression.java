package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.operands.Operand;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.values.Value;
import interpreter.model.values.operationinterfaces.Additive;
import interpreter.model.values.operationinterfaces.Logical;
import interpreter.model.values.operationinterfaces.Numeric;

import static interpreter.model.operands.Operand.AND;
import static interpreter.model.operands.Operand.OR;

public class LogicExpression implements Expression{
    Expression firstExpression, secondExpression;
    Operand operand;

    public LogicExpression(Expression firstExpression, Expression secondExpression, Operand operand) throws ExpressionException {
        super();
        if(!operand.logical()){
            throw new ExpressionException("Operand is not fit for a logical expression");
        }
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operand = operand;
    }
    public LogicExpression(Operand operand, Expression firstExpression, Expression secondExpression) throws ExpressionException {
        super();
        if(!operand.logical()){
            throw new ExpressionException("Operand is not fit for a logical expression");
        }
        this.operand = operand;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;

    }

    @Override
    public Value evaluate(ProgramState state) throws ExpressionException, ValueException, HeapException, SymbolTableException {
        Value firstValue = firstExpression.evaluate(state);
        if (firstValue instanceof Logical cast) {
            Value secondValue = secondExpression.evaluate(state);
            return switch (operand) {
                case OR -> cast.or(secondValue);
                case AND -> cast.and(secondValue);
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
        if ((!(operand == AND)) && (!(operand == OR))) {
            throw new TypecheckException("Unaccepted operand type for arithmetic expression -- %s".formatted(operand));
        }
        if (!(firstType.getDefault() instanceof Logical)){
            throw new TypecheckException("Expression does not evaluate to a logical type -- %s".formatted(firstType));
        }
        return firstType;
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new LogicExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }

    public String toString(){
        return "(" + firstExpression.toString() + " " + operand.toString() + " " + secondExpression.toString()+ ")";
    }
}
