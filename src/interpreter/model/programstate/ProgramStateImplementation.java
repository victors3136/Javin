package interpreter.model.programstate;

import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.executionstack.ExecutionStackDeque;
import interpreter.model.filetable.FileTable;
import interpreter.model.filetable.FileTableMap;
import interpreter.model.heaptable.HeapTable;
import interpreter.model.heaptable.HeapHashTable;
import interpreter.model.outputlist.OutputList;
import interpreter.model.outputlist.OutputListArray;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.symboltable.SymbolTableHashMap;
import interpreter.model.values.Value;

import java.text.MessageFormat;

public class ProgramStateImplementation implements ProgramState {
    private SymbolTable<String, Value> symbolTable;
    private ExecutionStack<Statement> executionStack;
    private OutputList<Value> outputList;
    private FileTable fileTable;
    private HeapTable heapTable;
    //private final Statement originalProgram;


    public ProgramStateImplementation(Statement originalProgram) {
        this.symbolTable = new SymbolTableHashMap<>();
        this.executionStack = new ExecutionStackDeque<>();
        this.outputList = new OutputListArray<>();
        this.fileTable = new FileTableMap();
        this.heapTable = new HeapHashTable();
        //this.originalProgram = originalProgram;
        executionStack.push(originalProgram);
    }

    @Override
    public void setSymbolTable(SymbolTable<String, Value> symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public void setExecutionStack(ExecutionStack<Statement> executionStack) {
        this.executionStack = executionStack;
    }

    @Override
    public void setOutputList(OutputList<Value> outputList) {
        this.outputList = outputList;
    }

    @Override
    public void setFileTable(FileTable fileTable) {
        this.fileTable = fileTable;
    }

    public OutputList<Value> getOutputList() {
        return outputList;
    }

    @Override
    public ExecutionStack<Statement> getExecutionStack() {
        return executionStack;
    }

    @Override
    public SymbolTable<String, Value> getSymbolTable() {
        return symbolTable;
    }


    @Override
    public FileTable getFileTable() {
        return fileTable;
    }

    @Override
    public void setHeapTable(HeapTable heapTable) {
        this.heapTable = heapTable;
    }

    @Override
    public HeapTable getHeapTable() {
        return heapTable;
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                """
                        Symbol Table : {0}
                        Execution Stack : {1}
                        File Table : {2}
                        Heap : {3}
                        Output List :
                        {4}
                        -----------------""", symbolTable.toString(),
                executionStack.toString(),
                fileTable.toString(),
                heapTable.toString(),
                outputList.toString());
    }
}
