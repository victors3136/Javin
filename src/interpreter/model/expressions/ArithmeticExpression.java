package interpreter.model.expressions;

import interpreter.model.operands.Operand;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;
import interpreter.model.values.operationinterfaces.Numeric;

public class ArithmeticExpression implements Expression {
    Expression firstExpression, secondExpression;
    Operand operand;

    public ArithmeticExpression(Expression firstExpression, Expression secondExpression, Operand operand) throws ExpressionException {
        super();
        if (!operand.arithmetic())
            throw new ExpressionException("Operand provided does not fit an arithmetic expression -- " + operand);
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operand = operand;
    }

    public ArithmeticExpression(Operand operand, Expression firstExpression, Expression secondExpression) throws ExpressionException {
        super();
        if (!operand.arithmetic())
            throw new ExpressionException("Operand provided does not fit an arithmetic expression -- " + operand);
        this.operand = operand;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Value evaluate(SymbolTable<String, Value> symbolTable) throws ExpressionException, ValueException {
        Value firstValue = firstExpression.evaluate(symbolTable);
        if (firstValue instanceof Numeric cast) {
            Value secondValue = secondExpression.evaluate(symbolTable);
            return switch (operand) {
                case ADD -> (Value) cast.add(secondValue);
                case SUB -> (Value) cast.sub(secondValue);
                case MUL -> (Value) cast.mul(secondValue);
                case DIV -> (Value) cast.div(secondValue);
                case EXP -> (Value) cast.exp(secondValue);
                default -> throw new ExpressionException("Unaccepted operand type");
            };
        }else throw new ExpressionException("First expression does not evaluate to a value suitable for the provided operand --" + firstExpression.toString() + ", operand " + operand.toString());
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new ArithmeticExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }


    public String toString() {
        return "(" + firstExpression.toString() + " " + operand.toString() + " " + secondExpression.toString() + " )";
    }
}
