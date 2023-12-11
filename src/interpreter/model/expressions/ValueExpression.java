package interpreter.model.expressions;

import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.values.Value;

public class ValueExpression implements Expression{
    final Value value;


    public ValueExpression(Value value){
        super();
        this.value = value;
    }
    @Override
    public Value evaluate(ProgramState state) {
        return value;
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) {
        return value.getType();
    }

    @Override
    public Expression deepCopy() {
        return new ValueExpression(value.deepCopy());
    }

    public String toString(){
        return value.toString() ;
    }
}
