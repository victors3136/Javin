package interpreter.model.values;

import interpreter.model.types.Type;
import interpreter.model.exceptions.ValueException;

public abstract class Value {
    Value(){}
    abstract public Type getType();
    public abstract boolean isOfType(Type t);

    public abstract String toString();

    public Value add(Value other) throws ValueException {
        throw new ValueException("Addition not allowed for this type -- "+ getType());
    }

    public Value sub(Value other) throws ValueException{
        throw new ValueException("Subtraction not allowed for this type -- " + getType());
    }
    public Value mul(Value other) throws ValueException{
        throw new ValueException("Multiplication not allowed for this type -- " + getType());
    }
    public Value div(Value other) throws ValueException{
        throw new ValueException("Division not allowed for this type -- "+ getType());
    }
    public Value exp(Value other) throws ValueException{
        throw new ValueException("Exponentiation not defined for this type -- "+ getType());
    }
    public Value or(Value other) throws ValueException{
        throw new ValueException("Logical 'OR' not allowed for this type -- "+ getType());
    }
    public Value and(Value other) throws ValueException{
        throw new ValueException("Logical 'AND' not allowed for this type -- "+ getType());
    }
    public Value equal(Value other) throws ValueException{
        throw new ValueException("Equality not defined for this type -- "+ getType());
    }
    public Value notEqual(Value other) throws ValueException{
        throw new ValueException("Inequality not defined for this type -- "+ getType());
    }
    public Value greater(Value other) throws ValueException{
        throw new ValueException("Comparison (>) not defined for this type -- "+ getType());
    }
    public Value lower(Value other) throws ValueException{
        throw new ValueException("Comparison (<) not defined for this type -- "+ getType());
    }
    public Value greaterOrEqual(Value other) throws ValueException{
        throw new ValueException("Comparison (>=) not defined for this type -- "+ getType());
    }
    public Value lowerOrEqual(Value other) throws ValueException{
        throw new ValueException("Comparison (<=) not defined for this type -- "+ getType());
    }

    abstract public Value deepCopy();
}
