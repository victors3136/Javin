package inputmanager;

import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.model.statements.Statement;
import interpreter.model.exceptions.ExpressionException;

public interface InputManager {
    Statement eatStringCreateProgram(String source) throws TokenizerException, ParseException, ExpressionException;
}
