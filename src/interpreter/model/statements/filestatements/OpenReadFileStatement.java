package interpreter.model.statements.filestatements;

import interpreter.model.expressions.Expression;
import interpreter.model.filetable.FileTable;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.exceptions.*;
import interpreter.model.values.StringValue;
import interpreter.model.values.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFileStatement implements Statement {
    final Expression filenameExpression;
    public OpenReadFileStatement(Expression expression){
        this.filenameExpression = expression;
    }


    @Override
    public String toString(){
        return "fopen( "+this.filenameExpression.toString()+" )";
    }
    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, HeapException {
        FileTable fileTable = state.getFileTable();
        Value value = filenameExpression.evaluate(state);
        if(!(value instanceof StringValue string))
            throw new StatementException("Argument given to an OpenReadFileStatement must evaluate to a string");
        if(fileTable.lookup(string.getValue())!=null){
            throw new StatementException("Trying to open a file which was already opened before -- "+ string.getValue());
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(string.getValue()));
            fileTable.add(string, bufferedReader);
        } catch (FileNotFoundException e) {
            throw new StatementException(e.getMessage());
        }
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new OpenReadFileStatement(this.filenameExpression.deepCopy());
    }
}
