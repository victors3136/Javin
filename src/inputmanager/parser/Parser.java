package inputmanager.parser;

import inputmanager.tokenstack.TokenStack;
import interpreter.model.statements.Statement;

public interface Parser {
    Statement program(TokenStack tokens) throws ParseException;
}
