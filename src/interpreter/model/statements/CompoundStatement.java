package interpreter.model.statements;
import interpreter.model.exceptions.StatementException;
import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.TypecheckException;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;

public class CompoundStatement implements Statement {
    final Statement firstStatement;
    final Statement secondStatement;
    public CompoundStatement(Statement firstStatement, Statement secondStatement){
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }
    @Override
    public ProgramState execute(ProgramState state) {
        ExecutionStack<Statement> executionStack = state.getExecutionStack();
        if(executionStack == null)
            return state;
        executionStack.push(secondStatement);
        executionStack.push(firstStatement);
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        return secondStatement.typecheck(firstStatement.typecheck(environment));
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new CompoundStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public String toString(){
        return firstStatement.toString()+" ; "+ secondStatement.toString();
    }
}
