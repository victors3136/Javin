package interpreter.model.statements;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.*;

public interface Statement {
    ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, TypeException;
    Statement deepCopy() throws ExpressionException;
}
