package interpreter.model.values;

import interpreter.model.types.Type;
import interpreter.model.exceptions.ValueException;

import java.util.Objects;

import static interpreter.model.types.Type.STRING;

public class StringValue extends Value {
    String value;
    public StringValue(String value) {
        StringBuilder buffer = new StringBuilder(value);
        if(buffer.charAt(0)=='\"'){
            buffer.deleteCharAt(0);
        }
        if(buffer.charAt(buffer.length()-1)=='\"'){
            buffer.deleteCharAt(buffer.length()-1);
        }
        this.value = buffer.toString();
    }
    public StringValue() {
        this.value = "";
    }

    public String getValue(){
        return value;
    }
    @Override
    public Type getType(){
        return STRING;
    }
    @Override
    public boolean isOfType(Type t) {
        return t==STRING;
    }

    @Override
    public String toString() {
        return "\""+value+"\"";
    }

    @Override
    public Value add(Value other) throws ValueException {
        if (!(other instanceof StringValue))
            throw new ValueException("Addition between string must feature 2 strings -- provided " + other.getType());
        return new StringValue(this.value + ((StringValue) other).value);
    }

    @Override
    public Value equal(Value other) throws ValueException {
        if (!(other instanceof StringValue))
            throw new ValueException("Cannot test string for equality with an instance of a different type -- provided " + other.getType());
        return new BoolValue(Objects.equals(this.value, ((StringValue) other).value));
    }

    @Override
    public Value notEqual(Value other) throws ValueException {
        if (!(other instanceof StringValue))
            throw new ValueException("Cannot test string for equality with an instance of a different type -- provided " + other.getType());
        return new BoolValue(! Objects.equals(this.value, ((StringValue) other).value));
    }
    @Override
    public Value greater(Value other) throws ValueException {
            if (!(other instanceof StringValue))
                throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
            return new BoolValue(this.value.compareTo(((StringValue) other).value) > 0);
    }

    @Override
    public Value lower(Value other) throws ValueException {
        if (!(other instanceof StringValue))
            throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
        return new BoolValue(this.value.compareTo(((StringValue) other).value) < 0);
    }

    @Override
    public Value greaterOrEqual(Value other) throws ValueException {
        if (!(other instanceof StringValue))
            throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
        return new BoolValue(this.value.compareTo(((StringValue) other).value) >= 0);
    }

    @Override
    public Value lowerOrEqual(Value other) throws ValueException {
        if (!(other instanceof StringValue))
            throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
        return new BoolValue(this.value.compareTo(((StringValue) other).value) <= 0);
    }

    @Override
    public Value deepCopy() {
        return new StringValue(this.value);
    }
}
