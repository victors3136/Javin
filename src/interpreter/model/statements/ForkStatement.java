package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;
import interpreter.model.programstate.ProgramStateImplementation;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;

public class ForkStatement implements Statement {

    final Statement targetStatement;

    public ForkStatement(Statement target) {
        this.targetStatement = target;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        return ProgramStateImplementation.forkProgram(targetStatement, state);
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        targetStatement.typecheck(environment.deepCopy());
        return environment;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new ForkStatement(targetStatement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(%s)".formatted(targetStatement.toString());
    }
}
