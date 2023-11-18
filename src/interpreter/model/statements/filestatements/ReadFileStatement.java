package interpreter.model.statements.filestatements;

import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.*;
import interpreter.model.values.BoolValue;
import interpreter.model.values.IntValue;
import interpreter.model.values.StringValue;
import interpreter.model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements Statement {
    Expression filenameExpression;
    String identifier;

    public ReadFileStatement(Expression filenameExpression, String identifier) {
        this.filenameExpression = filenameExpression;
        this.identifier = identifier;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, TypeException {
        SymbolTable<String, Value> symbolTable = state.getSymbolTable();
        if (symbolTable.lookup(this.identifier) == null) {
            throw new StatementException("Unknown identifier -- " + this.identifier);
        }
        Value value = filenameExpression.evaluate(symbolTable);
        if (!(value instanceof StringValue stringValue))
            throw new StatementException("File must be referenced through a string name -- got a(n) " + value.getType().toString());
        BufferedReader reader = state.getFileTable().lookup(stringValue.getValue());
        String readString, processed;
        try {
            readString = reader.readLine();
            processed = readString == null ? "0" : readString;
        } catch (IOException e) {
            throw new StatementException(e.getMessage() + " -- Reading error -- " + stringValue.getValue());
        }
        if (processed.isEmpty()) {
            symbolTable.update(this.identifier, symbolTable.lookup(this.identifier).getType().instantiateDefault());
        } else {
            switch (symbolTable.lookup(this.identifier).getType()) {
                case BOOLEAN -> {
                    symbolTable.update(this.identifier, new BoolValue(Boolean.parseBoolean(processed)));
                }
                case INTEGER -> {
                    symbolTable.update(this.identifier, new IntValue(Integer.parseInt(processed)));
                }
                case STRING -> {
                    symbolTable.update(this.identifier, new StringValue(processed));
                }
                default ->
                        throw new StatementException("Cannot assign " + processed + " to a null type -- " + this.identifier);
            }
        }
        return state;
    }

    @Override
    public String toString() {
        return "fread( " + this.filenameExpression.toString() + " " + this.identifier + " )";
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new ReadFileStatement(this.filenameExpression.deepCopy(), this.identifier);
    }
}
