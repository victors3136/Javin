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
    final Tokenizer tokenizer;
    final Parser parser;

    public InputManager() {
        this.tokenizer = new TripleStackTokenizer();
        this.parser = new RecursiveDescentParser();
    }

    @Override
    public Statement program(String source) throws TokenizerException, ParseException, ExpressionException {
        return this.parser.program(this.tokenizer.tokenize(source));
    }

}

