package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.operands.Operand;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.values.Value;
import interpreter.model.values.operationinterfaces.Additive;
import interpreter.model.values.operationinterfaces.Numeric;

import static interpreter.model.operands.Operand.ADD;

@SuppressWarnings("rawtypes")
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


    @Override
    @SuppressWarnings("rawtypes")
    public Value evaluate(ProgramState state) throws ExpressionException, ValueException, HeapException, SymbolTableException {
        Value firstValue = firstExpression.evaluate(state);

        if (operand == ADD && firstValue instanceof Additive additiveCast) {
            Value secondValue = secondExpression.evaluate(state);
            return (Value) additiveCast.add(secondValue);
        }
        if (firstValue instanceof Numeric numericCast) {
            Value secondValue = secondExpression.evaluate(state);
            return switch (operand) {
                case SUB -> (Value) numericCast.sub(secondValue);
                case MUL -> (Value) numericCast.mul(secondValue);
                case DIV -> (Value) numericCast.div(secondValue);
                case EXP -> (Value) numericCast.exp(secondValue);
                default -> throw new ExpressionException("Unaccepted operand type");
            };
        } else
            throw new ExpressionException("First expression does not evaluate to a value suitable for the provided operand --" + firstExpression.toString() + ", operand " + operand.toString());
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type t1 = firstExpression.typecheck(environment),
                t2 = secondExpression.typecheck(environment);
        if (!t1.equals(t2))
            throw new TypecheckException("Mismatched types -- %s, %s".formatted(t1, t2));
        switch (operand) {
            case ADD -> {
                if (t1.getDefault() instanceof Additive)
                    throw new TypecheckException("Expression does not evaluate to an additive type -- %s".formatted(t1));
                return t1;
            }
            case SUB, MUL, DIV ->{
                if (t1.getDefault() instanceof Numeric)
                    throw new TypecheckException("Expression does not evaluate to a numeric type -- %s".formatted(t1));
                return t1;
            }
            default -> throw new TypecheckException("Unaccepted operand type for arithmetic expression -- %s".formatted(operand));
        }
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new ArithmeticExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }


    public String toString() {
        return "(" + firstExpression.toString() + " " + operand.toString() + " " + secondExpression.toString() + " )";
    }
}
