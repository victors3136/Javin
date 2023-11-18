package interpreter.model.values;

import interpreter.model.exceptions.ValueException;
import interpreter.model.type.Type;
import interpreter.model.values.operationinterfaces.Testable;

public class ReferenceValue implements Value, Testable<Value>{

    Type inner;
    @Override
    public Type getType() {
        return new ReferenceType(inner);
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