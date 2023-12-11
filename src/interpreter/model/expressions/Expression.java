package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.utils.DeepCopiable;
import interpreter.model.values.Value;

public interface Expression extends DeepCopiable {
    Value evaluate(ProgramState state) throws ValueException, ExpressionException, HeapException, SymbolTableException;
    Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException;
    Expression deepCopy() throws ExpressionException;
}
