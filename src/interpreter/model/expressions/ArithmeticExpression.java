package interpreter.model.expressions;

import interpreter.model.operands.Operand;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;

public class ArithmeticExpression implements Expression{
    Expression firstExpression, secondExpression;
    Operand operand;

    public ArithmeticExpression(Expression firstExpression, Expression secondExpression, Operand operand) throws ExpressionException {
        super();
        if(!operand.arithmetic())
            throw new ExpressionException("Operand provided does not fit an arithmetic expression -- "+ operand);
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.operand = operand;
    }
    public ArithmeticExpression( Operand operand, Expression firstExpression, Expression secondExpression) throws ExpressionException {
        super();
        if(!operand.arithmetic())
            throw new ExpressionException("Operand provided does not fit an arithmetic expression -- "+ operand);
        this.operand = operand;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }
    @Override
    public Value evaluate(SymbolTable<String, Value> symbolTable) throws ExpressionException, ValueException {
        Value firstValue = firstExpression.evaluate(symbolTable);
        if(!firstValue.getType().isSuitableFor(operand))
            throw new ExpressionException("First expression does not evaluate to a value suitable for the provided operand --"+ firstExpression.toString() + ", operand "+ operand.toString() );
        Value secondValue = secondExpression.evaluate(symbolTable);
        if(!firstValue.isOfType(secondValue.getType()))
            throw new ValueException("Mismatched value types -- "+ firstValue + " " + secondValue);
        return
            switch(operand){
                case ADD -> firstValue.add(secondValue);
                case SUB -> firstValue.sub(secondValue);
                case MUL -> firstValue.mul(secondValue);
                case DIV -> firstValue.div(secondValue);
                case EXP -> firstValue.exp(secondValue);
                default-> throw new ExpressionException("Unaccepted operand type");
            };
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new ArithmeticExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }


    public String toString(){
        return "("+firstExpression.toString()+ " " + operand.toString() + " " + secondExpression.toString()+ " )";
    }
}
