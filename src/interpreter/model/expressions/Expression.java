package interpreter.model.expressions;

import interpreter.model.exceptions.HeapException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;

public interface Expression {
    Value evaluate(ProgramState state) throws ValueException, ExpressionException, HeapException;
    Expression deepCopy() throws ExpressionException;
}
