package interpreter.model.expressions;

import interpreter.model.symboltable.SymbolTable;
import interpreter.model.values.Value;

public class ValueExpression implements Expression{
    Value value;


    public ValueExpression(Value value){
        super();
        this.value = value;
    }
    @Override
    public Value evaluate(SymbolTable<String, Value>  symbolTable) {
        return value;
    }

    @Override
    public Expression deepCopy() {
        return new ValueExpression(value.deepCopy());
    }

    public String toString(){
        return value.toString() ;
    }
}
