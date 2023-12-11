package interpreter.model.statements;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;

public class NoOperationStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) {
        return environment;
    }

    @Override
    public Statement deepCopy() {
        return new NoOperationStatement();
    }

    @Override
    public String toString(){
        return " ";
    }
}
