package interpreter.model.values.operationinterfaces;

import interpreter.model.exceptions.ValueException;

public interface Additive<T> {
    T add(T other) throws ValueException;
}
