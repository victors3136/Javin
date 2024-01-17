package interpreter.model.programstate;

import interpreter.model.exceptions.*;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.executionstack.ExecutionStackDeque;
import interpreter.model.filetable.FileTable;
import interpreter.model.filetable.FileTableMap;
import interpreter.model.heaptable.HeapHashTable;
import interpreter.model.heaptable.HeapTable;
import interpreter.model.outputlist.OutputList;
import interpreter.model.outputlist.OutputListArray;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.symboltable.SymbolTableHashMap;
import interpreter.model.values.Value;

import java.text.MessageFormat;


public class ProgramStateImplementation implements ProgramState {
    private final int id;
    private static Integer ID_GENERATOR = 0;
    private static final Object LOCK = new Object();
    private SymbolTable<String, Value> symbolTable;
    private ExecutionStack<Statement> executionStack;
    private OutputList<Value> outputList;
    private FileTable fileTable;
    private HeapTable heapTable;

    public ProgramStateImplementation(Statement originalProgram) {
        synchronized (LOCK) {
            this.id = ID_GENERATOR++;
        }
        this.symbolTable = new SymbolTableHashMap<>();
        this.executionStack = new ExecutionStackDeque<>();
        this.outputList = new OutputListArray<>();
        this.fileTable = new FileTableMap();
        this.heapTable = new HeapHashTable();
        this.executionStack.push(originalProgram);
    }

    private ProgramStateImplementation(Statement target, SymbolTable<String, Value> symbolTable, HeapTable heapTable, FileTable fileTable, OutputList<Value> outputList) {
        synchronized (LOCK) {
            this.id = ID_GENERATOR++;
        }
        this.symbolTable = symbolTable.deepCopy();
        this.heapTable = heapTable;
        this.fileTable = fileTable;
        this.outputList = outputList;
        this.executionStack = new ExecutionStackDeque<>();
        this.executionStack.push(target);
    }

    public static ProgramState forkProgram(Statement target, ProgramState parentProgram) {
        return new ProgramStateImplementation(target, parentProgram.getSymbolTable(), parentProgram.getHeapTable(), parentProgram.getFileTable(), parentProgram.getOutputList());
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
    public boolean isNotCompleted() {
        return !executionStack.empty();
    }

    @Override
    public ProgramState takeOneStep() throws StatementException, ValueException, ExpressionException, HeapException, ProgramStateException, SymbolTableException {
        if (executionStack.empty()) {
            throw new ProgramStateException("Empty Stack when trying to take another step inside the program");
        }
        Statement currentStatement = executionStack.pop();
        return currentStatement.execute(this);
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                """
                        {0}
                        Symbol Table : {1}
                        Execution Stack : {2}
                        File Table : {3}
                        Heap : {4}
                        Output List :
                        {5}
                        -----------------""",
                getID().toString(),
                symbolTable.toString(),
                executionStack.toString(),
                fileTable.toString(),
                heapTable.toString(),
                outputList.toString());
    }

    public Integer getID() {
        return id;
    }
}
