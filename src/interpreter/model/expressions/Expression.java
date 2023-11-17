package interpreter.model.expressions;

import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;

public interface Expression {
    Value evaluate(SymbolTable<String, Value>  symbolTable) throws ValueException, ExpressionException;
    Expression deepCopy() throws ExpressionException;
}
