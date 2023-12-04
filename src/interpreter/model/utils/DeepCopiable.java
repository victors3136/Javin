package interpreter.model.utils;

import interpreter.model.exceptions.ExpressionException;

public interface DeepCopiable {
    Object deepCopy() throws ExpressionException;
}
