package interpreter.model.values.operationinterfaces;

import interpreter.model.exceptions.ValueException;
import interpreter.model.values.BoolValue;
import interpreter.model.values.Value;

public interface Testable<T> {
    BoolValue equal(T other) throws ValueException;

    BoolValue notEqual(T other) throws ValueException;
}
