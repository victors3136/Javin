package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;

class ScopeConclusionStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws ProgramStateException {
        state.getSymbolTable().removeOutOfScopeVariables(state.getCurrentScope());
        state.decCurrentScope();
        return state;
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
