package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;

class ScopeConclusionStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws SymbolTableException {
        state.getSymbolTable().removeOutOfScopeVariables();
        state.getSymbolTable().decScope();
        return null;
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
