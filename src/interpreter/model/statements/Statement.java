package interpreter.model.statements;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.*;

public interface Statement {
    ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, HeapException, ProgramStateException;
    Statement deepCopy() throws ExpressionException;
    String toString();
}
