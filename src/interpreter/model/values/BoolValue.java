package interpreter.model.values;

import interpreter.model.type.BoolType;
import interpreter.model.type.Type;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.operationinterfaces.Logical;
import interpreter.model.values.operationinterfaces.Testable;

public class BoolValue implements Value, Testable<Value>, Logical {

    final boolean value;

    public BoolValue() {
        super();
        value = false;
    }

    public BoolValue(boolean b) {
        super();
        value = b;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return BoolType.get();
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    public boolean isTrue() {
        return value;
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(this.value);
    }

    @Override
    public BoolValue or(Value other) throws ValueException {
        if (other instanceof BoolValue boolOther)
            return new BoolValue(this.value || boolOther.value);
        throw new ValueException("Cannot execute a logical 'AND' on a non-boolean type-- provided " + other.getType());

    }

    @Override
    public BoolValue and(Value other) throws ValueException {
        if (other instanceof BoolValue boolOther)
            return new BoolValue(this.value && boolOther.value);
        throw new ValueException("Cannot execute a logical 'AND' on a non-boolean type-- provided " + other.getType());
    }

    @Override
    public BoolValue not() {
        return new BoolValue(!this.value);
    }

    @Override
    public BoolValue equal(Value other) throws ValueException {
        if (other instanceof BoolValue boolOther)
            return new BoolValue(this.value == boolOther.value);
        throw new ValueException("Cannot test for equality between different types -- provided %s".formatted(other.getType()));
    }

    @Override
    public BoolValue notEqual(Value other) throws ValueException {
        return new BoolValue(!(this.equal(other).value));
    }
}
