package interpreter.model.programstate;

import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.filetable.FileTable;
import interpreter.model.heapmanager.HeapManager;
import interpreter.model.outputlist.OutputList;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.values.Value;

public interface ProgramState {

    void setOutputList(OutputList<Value> outputList);

    OutputList<Value> getOutputList();

    void setExecutionStack(ExecutionStack<Statement> executionStack);

    ExecutionStack<Statement> getExecutionStack();

    void setSymbolTable(SymbolTable<String, Value> symbolTable);

    SymbolTable<String, Value> getSymbolTable();

    void setFileTable(FileTable fileTable);

    FileTable getFileTable();

    void setHeapManager(HeapManager heapManager);

    HeapManager getHeapManager();
}
