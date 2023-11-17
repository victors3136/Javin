package interpreter.model.statements;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.types.Type;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.BoolValue;
import interpreter.model.values.Value;
import interpreter.model.exceptions.StatementException;

public class IfStatement implements Statement {
    Expression condition;
    Statement branchPositive, branchNegative;
    public IfStatement(Expression condition, Statement branchPositive, Statement branchNegative){
        this.condition = condition;
        this.branchPositive = branchPositive;
        this.branchNegative = branchNegative;
    }
    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException {
        ExecutionStack<Statement> stack = state.getExecutionStack();
        if(stack==null)
            return state;
        Value conditionValue = condition.evaluate(state.getSymbolTable());
        if(!conditionValue.isOfType(Type.BOOLEAN))
            throw new StatementException("Conditional statement does not evaluate to a boolean ");
        if(((BoolValue)conditionValue).isTrue())
            stack.push(branchPositive);
        else
            stack.push(branchNegative);
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new IfStatement(condition.deepCopy(), branchPositive.deepCopy(),branchNegative.deepCopy());
    }

    @Override
    public String toString(){
        return "if ( "+condition.toString()+" ) (\n"+ branchPositive.toString() + "\n) else (\n" + branchNegative.toString() + "\n)";
    }
}
