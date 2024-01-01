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
    final Expression firstExpression;
    final Expression secondExpression;
    final Operand operand;

    public ArithmeticExpression(Expression firstExpression, Expression secondExpression, Operand operand) {
        super();
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operand = operand;
    }

    public ArithmeticExpression(Operand operand, Expression firstExpression, Expression secondExpression) {
        super();
        this.operand = operand;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }


    @Override
    public Value evaluate(ProgramState state) throws ExpressionException, ValueException, HeapException, SymbolTableException {
        if (operand == ADD) {
            Additive firstValue = (Additive) firstExpression.evaluate(state);
            Additive secondValue = (Additive) secondExpression.evaluate(state);
            return (Value) firstValue.add(secondValue);
        }
        Numeric secondValue = (Numeric) secondExpression.evaluate(state);
        return switch (operand) {
            case SUB -> (Value) secondValue.sub(secondValue);
            case DIV -> (Value) secondValue.div(secondValue);
            case MUL -> (Value) secondValue.mul(secondValue);
            case EXP -> (Value) secondValue.exp(secondValue);
            default -> null;
        };
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type t1 = firstExpression.typecheck(environment),
                t2 = secondExpression.typecheck(environment);
        if (t1 != t2)
            throw new TypecheckException("Mismatched types -- %s, %s".formatted(t1, t2));
        switch (operand) {
            case ADD -> {
                if (!(t1.getDefault() instanceof Additive))
                    throw new TypecheckException("Expression does not evaluate to an additive type -- %s".formatted(t1));
                return t1;
            }
            case SUB, MUL, DIV, EXP -> {
                if (!(t1.getDefault() instanceof Numeric))
                    throw new TypecheckException("Expression does not evaluate to a numeric type -- %s".formatted(t1));
                return t1;
            }
            default ->
                    throw new TypecheckException("Unaccepted operand type for arithmetic expression -- %s".formatted(operand));
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
