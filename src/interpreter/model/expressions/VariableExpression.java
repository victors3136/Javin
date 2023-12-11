package interpreter.model.expressions;

import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.ValueException;
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
    public Value evaluate(ProgramState state) throws ValueException, SymbolTableException {
        Value lookup = state.getSymbolTable().lookup(identifier);
        if (lookup == null)
            throw new ValueException("Unable to evaluate the identifier -- " + identifier);
        return lookup;
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
