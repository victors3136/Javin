package interpreter.model.values.operationinterfaces;

import interpreter.model.exceptions.ValueException;

public interface Numeric<T> extends Additive<T> {
    T sub(T other) throws ValueException;

    T mul(T other) throws ValueException;

    T div(T other) throws ValueException;
    T exp(T other) throws ValueException;
}
