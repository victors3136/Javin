package inputmanager;

import inputmanager.parser.ParseException;
import inputmanager.parser.Parser;
import inputmanager.parser.RecursiveDescentParser;
import inputmanager.tokenizer.Tokenizer;
import inputmanager.tokenizer.TokenizerException;
import inputmanager.tokenizer.TripleStackTokenizer;
import interpreter.model.statements.Statement;
import interpreter.model.exceptions.ExpressionException;

public class InputManager implements StringToStatementConverter {
    static final Tokenizer tokenizer = new TripleStackTokenizer();
    static final Parser parser = new RecursiveDescentParser();

    public InputManager() {
    }

    @Override
    public Statement program(String source) throws TokenizerException, ParseException, ExpressionException {
        return parser.program(tokenizer.tokenize(source));
    }

}

