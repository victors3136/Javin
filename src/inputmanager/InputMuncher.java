package inputmanager;

import inputmanager.parser.ParseException;
import inputmanager.parser.Parser;
import inputmanager.parser.RecursiveDescentParser;
import inputmanager.tokenizer.Tokenizer;
import inputmanager.tokenizer.TokenizerException;
import inputmanager.tokenizer.TripleStackTokenizer;
import interpreter.model.statements.Statement;
import interpreter.model.exceptions.ExpressionException;

public class InputMuncher implements InputManager {
    final Tokenizer tokenizer;
    final Parser parser;

    public InputMuncher() {
        tokenizer = new TripleStackTokenizer();
        parser = new RecursiveDescentParser();
    }

    @Override
    public Statement eatStringCreateProgram(String source) throws TokenizerException, ParseException, ExpressionException {
        return parser.buildProgram(tokenizer.tokenize(source));
    }

    ;
}

