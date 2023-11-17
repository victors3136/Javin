package interpreter.model.values;

import interpreter.model.types.Type;
import interpreter.model.exceptions.ValueException;

public class BoolValue extends Value{

    boolean value;

    public BoolValue() {
        super();
        value = false;
    }

    public BoolValue(boolean b) {
        super();
        value = b;
    }

    public boolean getValue(){
        return value;
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public boolean isOfType(Type t) {
        return t == Type.BOOLEAN;
    }

    @Override
    public String toString(){
        return Boolean.toString(value);
    }
    public boolean isTrue(){
        return value;
    }

    @Override
    public Value equal(Value other) throws ValueException {
        if(! (other instanceof BoolValue))
            throw new ValueException("Cannot test boolean for equality with an instance of a different type -- provided "+ other.getType());
        return new BoolValue(this.value == ((BoolValue) other).value );
    }

    @Override
    public Value notEqual(Value other) throws ValueException {
        if(! (other instanceof BoolValue))
            throw new ValueException("Cannot test boolean for equality with an instance of a different type -- provided "+ other.getType());
        return new BoolValue(this.value != ((BoolValue) other).value );
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(this.value);
    }

    @Override
    public Value or(Value other) throws ValueException {
        if(! (other instanceof BoolValue))
            throw new ValueException("Cannot execute a logical 'OR' on a non-boolean type-- provided "+ other.getType());
        return new BoolValue(this.value || ((BoolValue) other).value );
    }

    @Override
    public Value and(Value other) throws ValueException {
        if(! (other instanceof BoolValue))
            throw new ValueException("Cannot execute a logical 'AND' on a non-boolean type-- provided "+ other.getType());
        return new BoolValue(this.value && ((BoolValue) other).value );
    }
}
