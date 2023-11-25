package interpreter.model.values;

import interpreter.model.exceptions.ValueException;
import interpreter.model.type.IntType;
import interpreter.model.type.Type;
import interpreter.model.values.operationinterfaces.Additive;
import interpreter.model.values.operationinterfaces.Numeric;
import interpreter.model.values.operationinterfaces.Testable;
import interpreter.model.values.operationinterfaces.Comparable;

public class IntValue implements Value, Numeric<Value>, Additive<Value>, Testable<Value>, Comparable<Value> {

    final int value;

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
        return IntType.get();
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public Value add(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new IntValue(this.value + intOther.value);
        }
        throw new ValueException("Adding to an integer must be done using another integer-- provided %s".formatted(other.getType()));
    }

    @Override
    public Value sub(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new IntValue(this.value - intOther.value);
        }
        throw new ValueException("Subtracting from an integer must be done using another integer-- provided %s".formatted(other.getType()));
    }

    @Override
    public Value mul(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new IntValue(this.value * intOther.value);
        }
        throw new ValueException("Multiplying an integer must be done using another integer-- provided %s".formatted(other.getType()));
    }

    @Override
    public Value div(Value other) throws ValueException {

        if (other instanceof IntValue intOther) {
            if (intOther.value == 0)
                throw new ValueException("Division by 0");
            return new IntValue(this.value / intOther.value);
        }
        throw new ValueException("Dividing an integer must be done using another integer-- provided %s".formatted(other.getType()));
    }

    private static int fastExp(int base, int exp) {
        return switch (exp) {
            case 0 -> 1;
            case 1 -> base;
            default -> {
                var temp = fastExp(base, exp / 2);
                yield ((exp % 2 == 1) ? base : 1) * temp * temp;
            }
        };
    }

    private static int fastNegativeExp(int base, int exp) throws ValueException {
        if (base == 0)
            throw new ValueException("Cannot exponentiate 0 with a negative exp");
        return switch (exp) {
            case 0 -> 1;
            case -1 -> 1 / base;
            default -> {
                var temp = fastNegativeExp(base, exp / 2);
                yield ((exp % 2 == 1) ? base : 1) * temp * temp;
            }
        };
    }

    @Override
    public Value exp(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            var var = (intOther.value > 0) ? fastExp(this.value, intOther.value) : fastNegativeExp(this.value, intOther.value);
            return new IntValue(var);
        } else throw new ValueException("When raising to a power, the base and exponent must be of the same type");
    }

    @Override
    public BoolValue equal(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new BoolValue(this.value == intOther.value);
        }
        throw new ValueException("Cannot test integer for equality with an instance of a different type -- provided " + other.getType());
    }

    @Override
    public BoolValue notEqual(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new BoolValue(this.value != intOther.value);
        }
        throw new ValueException("Cannot test integer for inequality with an instance of a different type -- provided " + other.getType());
    }

    @Override
    public BoolValue greater(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new BoolValue(this.value > intOther.value);
        }
        throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
    }

    @Override
    public BoolValue lower(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new BoolValue(this.value < intOther.value);
        }
        throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
    }

    @Override
    public BoolValue greaterOrEqual(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new BoolValue(this.value >= intOther.value);
        }
        throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
    }

    @Override
    public BoolValue lowerOrEqual(Value other) throws ValueException {
        if (other instanceof IntValue intOther) {
            return new BoolValue(this.value <= intOther.value);
        }
        throw new ValueException("Cannot compare integer with an instance of different type -- provided " + other.getType());
    }

    @Override
    public Value deepCopy() {
        return new IntValue(this.value);
    }
}
