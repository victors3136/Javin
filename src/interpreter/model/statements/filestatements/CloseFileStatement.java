package interpreter.model.statements.filestatements;

import interpreter.model.expressions.Expression;
import interpreter.model.filetable.FileTable;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.exceptions.*;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.values.StringValue;
import interpreter.model.values.Value;

import java.io.IOException;

public class CloseFileStatement implements Statement {
    private final Expression filenameExpression;

    public CloseFileStatement(Expression filenameExpression){
        this.filenameExpression = filenameExpression;
    }

    @Override
    public String toString(){
        return "fclose( "+this.filenameExpression.toString()+" )";
    }
    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, HeapException, SymbolTableException {
        StringValue string = (StringValue) filenameExpression.evaluate(state);
        String fileIdentifier = string.getValue();
        FileTable fileTable = state.getFileTable();
        try {
            fileTable.lookup(fileIdentifier).close();
        } catch (IOException e) {
            throw new StatementException(e.getMessage()+" -- "+fileIdentifier);
        }
        fileTable.remove(fileIdentifier);
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type type = filenameExpression.typecheck(environment);
        if (!(type.getDefault() instanceof StringValue)) {
            throw new TypecheckException("Argument given to an CloseFileStatement must evaluate to a string");
        }
        return environment;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new CloseFileStatement(this.filenameExpression.deepCopy());
    }
}
