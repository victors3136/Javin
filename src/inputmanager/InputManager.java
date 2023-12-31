package inputmanager;

import inputmanager.parser.ParseException;
import inputmanager.parser.Parser;
import inputmanager.parser.RecursiveDescentParser;
import inputmanager.tokenizer.Tokenizer;
import inputmanager.tokenizer.TokenizerException;
import inputmanager.tokenizer.TripleStackTokenizer;
import interpreter.controller.Controller;
import interpreter.controller.ControllerImplementation;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.programstate.ProgramState;
import interpreter.model.programstate.ProgramStateImplementation;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTableHashMap;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryVector;

public class InputManager implements StringToStatementConverter {
    static final Tokenizer tokenizer = new TripleStackTokenizer();
    static final Parser parser = new RecursiveDescentParser();

    public InputManager() {
    }

    public Statement parse(String source) throws TokenizerException, ParseException {
        return parser.program(tokenizer.tokenize(source));
    }
    @Override
    public Controller program(String source, int counter){
        Statement program;
        try {
            program = parse(source);
        } catch (TokenizerException | ParseException e) {
            System.err.println(e.getMessage());
            return null;
        }
        try {
            program.typecheck(new SymbolTableHashMap<>());
        } catch (TypecheckException e) {
            System.err.println(e.getMessage());
            return null;
        }
        ProgramState programState = new ProgramStateImplementation(program);
        Repository repository = new RepositoryVector("logs/log%d.txt".formatted(counter));
        return new ControllerImplementation(programState, repository);
    }
}

