package interpreter.model.values.operationinterfaces;

import interpreter.model.exceptions.ValueException;
import interpreter.model.values.BoolValue;
import interpreter.model.values.Value;

public interface Logical {
    BoolValue or(Value other) throws ValueException;

    BoolValue and(Value other) throws ValueException;

    BoolValue not();
}
