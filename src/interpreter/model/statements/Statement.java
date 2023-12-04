package interpreter.model.statements;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.*;
import interpreter.model.utils.DeepCopiable;

public interface Statement extends DeepCopiable {
    ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, HeapException;
    Statement deepCopy() throws ExpressionException;
    String toString();
}
