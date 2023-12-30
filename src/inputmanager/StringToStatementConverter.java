package inputmanager;

import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.model.statements.Statement;

public interface StringToStatementConverter {
    Statement program(String source) throws TokenizerException, ParseException;
}
