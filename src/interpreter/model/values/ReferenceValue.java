package interpreter.model.values;

import interpreter.model.exceptions.ValueException;
import interpreter.model.type.Type;
import interpreter.model.values.operationinterfaces.Testable;

public class ReferenceValue implements Value, Testable<Value>{
    int address;
    Type locationType;
    public ReferenceValue(int address, Type referredType){
        this.address = address;
        this.locationType = referredType;
    }
    public int getAddress(){
        return address;
    }
    @Override
    public Type getType() {
        return locationType;
    }

    @Override
    public Value deepCopy() {
        return null;
    }

    @Override
    public BoolValue equal(Value other) throws ValueException {
        return null;
    }

    @Override
    public BoolValue notEqual(Value other) throws ValueException {
        return null;
    }
}