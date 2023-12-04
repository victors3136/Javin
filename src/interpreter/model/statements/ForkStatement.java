package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;
import interpreter.model.programstate.ProgramStateImplementation;

public class ForkStatement implements Statement {

    Statement targetStatement;

    public ForkStatement(Statement nu) {
        this.targetStatement = nu;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, HeapException {
        return ProgramStateImplementation.forkProgram(targetStatement, state);
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new ForkStatement(targetStatement.deepCopy());
    }

    @Override
    public String toString() {
        return "";
    }
}
