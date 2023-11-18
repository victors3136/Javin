package interpreter.model.values.operationinterfaces;

import interpreter.model.exceptions.ValueException;
import interpreter.model.values.BoolValue;

public interface Comparable<T> extends Testable<T> {
    BoolValue greater(T elem) throws ValueException;

    BoolValue lower(T elem) throws ValueException;

    BoolValue greaterOrEqual(T elem) throws ValueException;

    BoolValue lowerOrEqual(T elem) throws ValueException;
}
