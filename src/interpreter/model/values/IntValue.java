package interpreter.model.values;

import interpreter.model.types.Type;
import interpreter.model.exceptions.ValueException;

public class IntValue extends Value {

    int value;

    public IntValue() {
        super();
        value = 0;
    }

    public IntValue(int i) {
        super();
        value = i;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    public boolean isOfType(Type t) {
        return t == Type.INTEGER;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public Value add(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Adding to an integer must be done using another integer-- provided " + other.getType());
        return new IntValue(this.value + ((IntValue) other).value);
    }

    @Override
    public Value sub(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Subtracting from an integer must be done using another integer -- provided " + other.getType());
        return new IntValue(this.value - ((IntValue) other).value);
    }

    @Override
    public Value mul(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Multiplying an integer must be done using another integer -- provided " + other.getType());
        return new IntValue(this.value * ((IntValue) other).value);
    }

    @Override
    public Value div(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Dividing an integer must be done using another integer-- provided " + other.getType());
        if (((IntValue) other).value == 0)
            throw new ValueException("Division by 0");
        return new IntValue(this.value / ((IntValue) other).value);
    }

    @Override
    public Value exp(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Division allowed only on integers -- provided " + other.getType());
        int exponent = ((IntValue) other).value;
        int base = this.value;
        int result = 1;
        if (exponent > 0)
            while (exponent > 0) {
                result *= base;
                exponent--;
            }
        else if (exponent < 0) {
            if (base == 0)
                throw new ValueException("Division by 0");
            while (exponent < 0) {
                result /= base;
                exponent ++;
            }
        }
        return new IntValue(result);
    }

    @Override
    public Value equal(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Cannot test integer for equality with an instance of a different type -- provided " + other.getType());
        return new BoolValue(this.value == ((IntValue) other).value);
    }

    @Override
    public Value notEqual(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Cannot test integer for inequality with an instance of a different type -- provided " + other.getType());
        return new BoolValue(this.value != ((IntValue) other).value);
    }

    @Override
    public Value greater(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
        return new BoolValue(this.value > ((IntValue) other).value);
    }

    @Override
    public Value lower(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
        return new BoolValue(this.value < ((IntValue) other).value);
    }

    @Override
    public Value greaterOrEqual(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
        return new BoolValue(this.value >= ((IntValue) other).value);
    }

    @Override
    public Value lowerOrEqual(Value other) throws ValueException {
        if (!(other instanceof IntValue))
            throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
        return new BoolValue(this.value <= ((IntValue) other).value);
    }

    @Override
    public Value deepCopy() {
        return new IntValue(this.value);
    }
}
