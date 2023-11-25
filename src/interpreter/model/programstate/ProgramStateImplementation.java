package interpreter.model.programstate;

import interpreter.model.exceptions.ProgramStateException;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.executionstack.ExecutionStackDeque;
import interpreter.model.filetable.FileTable;
import interpreter.model.filetable.FileTableMap;
import interpreter.model.heapmanager.HeapManager;
import interpreter.model.heapmanager.HeapManagerTable;
import interpreter.model.outputlist.OutputList;
import interpreter.model.outputlist.OutputListArray;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.symboltable.SymbolTableHashMap;
import interpreter.model.values.Value;

public class ProgramStateImplementation implements ProgramState {

    private int currentScope;
    private static final int MAX_SCOPE = 1024;
    private SymbolTable<String, Value, Integer> symbolTable;
    private ExecutionStack<Statement> executionStack;
    private OutputList<Value> outputList;
    private FileTable fileTable;
    private HeapManager heapManager;
    private final Statement originalProgram;


    public ProgramStateImplementation(Statement originalProgram) {
        this.currentScope = 0;
        this.symbolTable = new SymbolTableHashMap<>();
        this.executionStack = new ExecutionStackDeque<>();
        this.outputList = new OutputListArray<>();
        this.fileTable = new FileTableMap();
        this.heapManager = new HeapManagerTable();
        this.originalProgram = originalProgram;
        executionStack.push(this.originalProgram);
    }

    @Override
    public void setSymbolTable(SymbolTable<String, Value, Integer> symbolTable) {
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
    public SymbolTable<String, Value, Integer> getSymbolTable() {
        return symbolTable;
    }


    @Override
    public FileTable getFileTable() {
        return fileTable;
    }

    @Override
    public void setHeapManager(HeapManager heapManager) {
        this.heapManager = heapManager;
    }

    @Override
    public HeapManager getHeapManager() {
        return heapManager;
    }

    @Override
    public int getCurrentScope() {
        return this.currentScope;
    }

    @Override
    public void incCurrentScope() throws ProgramStateException {
        if (this.currentScope >= MAX_SCOPE)
            throw new ProgramStateException("Scope overflow -- current scope: %d".formatted(currentScope));
        ++currentScope;
    }

    @Override
    public void decCurrentScope() throws ProgramStateException {
        if (this.currentScope <= 0)
            throw new ProgramStateException("Scope underflow -- current scope: %d".formatted(currentScope));
        --currentScope;
    }

    @Override
    public String toString() {
        return "┏━━━━━━━━━━━━━━━━━━━┓\n" +
                "┃\tSymbol Table:\t┃\n" +
                "┗━━━━━━━━━━━━━━━━━━━┛\n" +
                symbolTable.toString() +
                "┏━━━━━━━━━━━━━━━━━━━━━━━┓\n" +
                "┃\tExecution Stack:\t┃\n" +
                "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n" +
                executionStack.toString() +
                "┏━━━━━━━━━━━━━━━┓\n" +
                "┃\tOutput List\t┃\n" +
                "┗━━━━━━━━━━━━━━━┛\n" +
                outputList.toString() +
                "┏━━━━━━━━━━━━━━━┓\n" +
                "┃\tFile  Table\t┃\n" +
                "┗━━━━━━━━━━━━━━━┛\n" +
                fileTable.toString() +
                "━━━━━━━━━━━━━━━━━━━\n";
    }
}
