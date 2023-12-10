package interpreter.model.programstate;

import interpreter.model.exceptions.*;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.filetable.FileTable;
import interpreter.model.heaptable.HeapTable;
import interpreter.model.outputlist.OutputList;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.values.Value;

public interface ProgramState {

    @SuppressWarnings("unused")
    void setOutputList(OutputList<Value> outputList);

    OutputList<Value> getOutputList();

    @SuppressWarnings("unused")
    void setExecutionStack(ExecutionStack<Statement> executionStack);

    ExecutionStack<Statement> getExecutionStack();

    @SuppressWarnings("unused")
    void setSymbolTable(SymbolTable<String, Value> symbolTable);

    SymbolTable<String, Value> getSymbolTable();

    @SuppressWarnings("unused")
    void setFileTable(FileTable fileTable);

    FileTable getFileTable();

    void setHeapTable(HeapTable heapTable);

    HeapTable getHeapTable();

    boolean isNotCompleted();

    ProgramState takeOneStep() throws ProgramStateException, SymbolTableException, StatementException, ValueException, ExpressionException, HeapException;


}
