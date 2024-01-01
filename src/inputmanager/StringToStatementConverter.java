package inputmanager;

import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.model.exceptions.TypecheckException;

public interface StringToStatementConverter {
    Controller program(String source, int counter) throws TokenizerException, ParseException, TypecheckException;
}
