package interpreter.model.statements;

import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;

public class VariableDeclarationStatement implements Statement {
    final String identifier;
    final Type type;


    public VariableDeclarationStatement(Type t, String id) {
        type = t;
        identifier = id;
    }

    @Override
    public ProgramState execute(ProgramState state) throws SymbolTableException {
        state.getSymbolTable().put(identifier, type.getDefault());
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        try {
            environment.put(identifier,type);
        } catch (SymbolTableException ste) {
            throw new TypecheckException(ste.getMessage());
        }
        return environment;
    }

    @Override
    public Statement deepCopy() {
        return new VariableDeclarationStatement(type.deepCopy(), identifier);
    }

    @Override
    public String toString() {
        return type.toString() + " " + identifier;
    }
}
