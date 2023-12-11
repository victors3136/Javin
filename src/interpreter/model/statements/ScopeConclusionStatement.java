package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;

class ScopeConclusionStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws SymbolTableException {
        state.getSymbolTable().removeOutOfScopeVariables();
        state.getSymbolTable().decScope();
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) {
        return environment;
    }

    @Override
    public Statement deepCopy() {
        return new ScopeConclusionStatement();
    }

    @Override
    public String toString() {
        return "# -- end of enclosing scope ";
    }
}
