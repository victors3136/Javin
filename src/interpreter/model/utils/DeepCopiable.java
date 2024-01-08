package interpreter.model.utils;

import interpreter.model.exceptions.ExpressionException;

@SuppressWarnings("unused")
public interface DeepCopiable {
    Object deepCopy() throws ExpressionException;
}
