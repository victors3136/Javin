package interpreter.model.expressions;

import interpreter.model.operands.Operand;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;

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
    public Value evaluate(SymbolTable<String, Value>  symbolTable) throws ExpressionException, ValueException {
        Value firstValue = firstExpression.evaluate(symbolTable);
        if(!firstValue.getType().isSuitableFor(operand)){
            throw new ExpressionException("First expression does not evaluate to a value of type suitable for the provided operand -- "+ firstExpression.toString() + ", operand "+ operand.toString());
        }
        Value secondValue = secondExpression.evaluate(symbolTable);
        if(!firstValue.isOfType(secondValue.getType()))
            throw new ExpressionException("The two expressions do not evaluate to the same type -- " +firstValue.getType()+", "+ secondValue.getType());
        return switch(operand){
            case OR -> firstValue.or(secondValue);
            case AND -> firstValue.and(secondValue);
            default -> throw new ExpressionException("Operand provided is not suitable for a logical expression");
        };
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new LogicExpression(firstExpression.deepCopy(), secondExpression.deepCopy(), operand.deepCopy());
    }

    public String toString(){
        return "(" + firstExpression.toString() + " " + operand.toString() + " " + secondExpression.toString()+ ")";
    }
}
