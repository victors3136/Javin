package interpreter.model.type;

import interpreter.model.values.Value;
public class Type {
    private final Class<? extends Value> valueClass;

    public Type(Class<? extends Value> valueClass) {
        this.valueClass = valueClass;
    }

    public String toString() {
        return valueClass.toString();
    }

    public boolean equals(Class<? extends Value> other) {
        return other.isAssignableFrom(valueClass);
    }

    public Type deepCopy() {
        return new Type(valueClass);
    }
}