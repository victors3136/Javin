package inputmanager;

import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.statements.Statement;

public interface InputManager {
    Controller program(String source, int counter) throws TokenizerException, ParseException, TypecheckException;
    Controller program(Statement source, int counter)throws TypecheckException;
}
