package interpreter.model.programstate;

import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.executionstack.ExecutionStackDeque;
import interpreter.model.filetable.FileTable;
import interpreter.model.filetable.FileTableMap;
import interpreter.model.outputlist.OutputList;
import interpreter.model.outputlist.OutputListArray;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.symboltable.SymbolTableHashMap;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.values.Value;

public class ProgramStateImplementation implements ProgramState{

    private SymbolTable<String,Value> symbolTable;
    private ExecutionStack<Statement> executionStack;
    private OutputList<Value> outputList;
    private FileTable fileTable;
    private final Statement originalProgram;

    public ProgramStateImplementation(Statement originalProgram){
        this.symbolTable = new SymbolTableHashMap<>();
        this.executionStack = new ExecutionStackDeque<>();
        this.outputList = new OutputListArray<>();
        this.fileTable = new FileTableMap();
        this.originalProgram = originalProgram;
        executionStack.push(this.originalProgram);

    }
    public ProgramStateImplementation(SymbolTable<String, Value> symbolTable, ExecutionStack<Statement> executionStack, OutputList<Value> outputList, FileTable fileTable, Statement originalProgram) throws ExpressionException {
        this.symbolTable = symbolTable;
        this.executionStack = executionStack;
        this.outputList = outputList;
        this.fileTable = fileTable;
        this.originalProgram = originalProgram.deepCopy();
        this.executionStack.push(this.originalProgram);
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
    public String toString(){
        return "┏━━━━━━━━━━━━━━━━━━━┓\n"+
                "┃\tSymbol Table:\t┃\n"+
                "┗━━━━━━━━━━━━━━━━━━━┛\n" +
                symbolTable.toString() +
                "┏━━━━━━━━━━━━━━━━━━━━━━━┓\n" +
                "┃\tExecution Stack:\t┃\n" +
                "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n" +
                executionStack.toString() +
                "┏━━━━━━━━━━━━━━━┓\n" +
                "┃\tOutput List\t┃\n"+
                "┗━━━━━━━━━━━━━━━┛\n" +
                outputList.toString() +
                "┏━━━━━━━━━━━━━━━┓\n" +
                "┃\tFile  Table\t┃\n"+
                "┗━━━━━━━━━━━━━━━┛\n" +
                fileTable.toString()+
                "━━━━━━━━━━━━━━━━━━━\n";
    }
}
