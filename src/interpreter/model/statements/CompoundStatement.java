package interpreter.model.statements;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.ExpressionException;

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
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new CompoundStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public String toString(){
        return firstStatement.toString()+";\n"+ secondStatement.toString();
    }
}
