package interpreter.model.expressions;

import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.values.Value;

public class VariableExpression implements Expression {
    final String identifier;

    public VariableExpression(String identifier) {
        super();
        this.identifier = identifier;
    }

    @Override
    public Value evaluate(ProgramState state) throws SymbolTableException {
        return state.getSymbolTable().lookup(identifier);
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        try {
            return environment.lookup(identifier);
        } catch (SymbolTableException ste) {
            throw new TypecheckException(ste.getMessage());
        }
    }

    @Override
    public Expression deepCopy() {
        return new VariableExpression(identifier);
    }

    public String toString() {
        return identifier;
    }
}
