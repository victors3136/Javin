package interpreter.model.expressions;

import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;

public class VariableExpression implements Expression{
    String identifier;

    public VariableExpression(String identifier) {
        super();
        this.identifier = identifier;
    }

    @Override
    public Value evaluate(ProgramState state) throws ValueException {
        Value lookup = state.getSymbolTable().lookup(identifier);
        if(lookup == null)
            throw new ValueException("Unable to evaluate the identifier -- " + identifier );
        return lookup;
    }

    @Override
    public Expression deepCopy() {
        return new VariableExpression(identifier);
    }

    public String toString(){
        return identifier;
    }
}
