package interpreter.model.values;

import interpreter.model.type.Type;

public interface Value {
    Type getType();

    String toString();

    Value deepCopy();
}
