package interpreter.model.statements;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.*;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.utils.DeepCopiable;

public interface Statement extends DeepCopiable {
    ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, HeapException;
    SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException;
    Statement deepCopy() throws ExpressionException;
    String toString();
}
