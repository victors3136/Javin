package interpreter.model.values;

import interpreter.model.exceptions.ValueException;
import interpreter.model.type.Type;
import interpreter.model.values.operationinterfaces.Additive;
import interpreter.model.values.operationinterfaces.Testable;
import interpreter.model.values.operationinterfaces.Comparable;
import java.util.Objects;

public class StringValue implements Value, Additive<Value>, Comparable<Value>, Testable<Value> {
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
        return new Type(IntValue.class);
    }

    @Override
    public String toString() {
        return "\""+value+"\"";
    }

    @Override
    public Value add(Value other) throws ValueException {
        if (other instanceof StringValue stringValue) {
            return new StringValue(this.value + stringValue.value);
        }
        throw new ValueException("Addition between strings must feature two strings -- provided " + other.getType());
    }

    @Override
    public BoolValue equal(Value other) throws ValueException {
        if (other instanceof StringValue stringValue) {
            return new BoolValue(Objects.equals(this.value, stringValue.value));
        }
        throw new ValueException("Cannot test string for equality with an instance of a different type -- provided " + other.getType());
    }

    @Override
    public BoolValue notEqual(Value other) throws ValueException {
        if (other instanceof StringValue stringValue) {
            return new BoolValue(!Objects.equals(this.value, stringValue.value));
        }
        throw new ValueException("Cannot test string for inequality with an instance of a different type -- provided " + other.getType());
    }

    @Override
    public BoolValue greater(Value other) throws ValueException {
        if (other instanceof StringValue stringValue) {
            return new BoolValue(this.value.compareTo(stringValue.value) > 0);
        }
        throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
    }

    @Override
    public BoolValue lower(Value other) throws ValueException {
        if (other instanceof StringValue stringValue) {
            return new BoolValue(this.value.compareTo(stringValue.value) < 0);
        }
        throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
    }

    @Override
    public BoolValue greaterOrEqual(Value other) throws ValueException {
        if (other instanceof StringValue stringValue) {
            return new BoolValue(this.value.compareTo(stringValue.value) >= 0);
        }
        throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
    }

    @Override
    public BoolValue lowerOrEqual(Value other) throws ValueException {
        if (other instanceof StringValue stringValue) {
            return new BoolValue(this.value.compareTo(stringValue.value) <= 0);
        }
        throw new ValueException("Cannot compare string with an instance of a different type -- provided " + other.getType());
    }


    @Override
    public Value deepCopy() {
        return new StringValue(this.value);
    }
}
