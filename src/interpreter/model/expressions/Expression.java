package interpreter.model.expressions;

import interpreter.model.exceptions.HeapException;
import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;

public interface Expression {
    Value evaluate(ProgramState state) throws ValueException, ExpressionException, HeapException, SymbolTableException;
    Expression deepCopy() throws ExpressionException;
}
