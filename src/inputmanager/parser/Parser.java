package inputmanager.parser;

import inputmanager.tokenstack.TokenStack;
import interpreter.model.statements.Statement;
import interpreter.model.exceptions.ExpressionException;

public interface Parser {
    Statement buildProgram(TokenStack tokens) throws ParseException, ExpressionException;
}
