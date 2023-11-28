package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.values.BoolValue;
import interpreter.model.values.Value;

public class IfStatement implements Statement {
    final Expression condition;
    final Statement branchPositive;
    final Statement branchNegative;

    public IfStatement(Expression condition, Statement branchPositive, Statement branchNegative) {
        this.condition = condition;
        this.branchPositive = branchPositive;
        this.branchNegative = branchNegative;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, HeapException, SymbolTableException {
        ExecutionStack<Statement> stack = state.getExecutionStack();
        if (stack == null)
            return state;
        Value conditionValue = condition.evaluate(state);
        if (!(conditionValue instanceof BoolValue))
            throw new StatementException("Conditional statement does not evaluate to a boolean ");
        stack.push(new ScopeConclusionStatement());
        if (((BoolValue) conditionValue).isTrue())
            stack.push(branchPositive);
        else
            stack.push(branchNegative);
        state.getSymbolTable().incScope();
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new IfStatement(condition.deepCopy(), branchPositive.deepCopy(), branchNegative.deepCopy());
    }

    @Override
    public String toString() {
        return "if ( " + condition.toString() + " ) ( " + branchPositive.toString() + " ) else ( " + branchNegative.toString() + " )";
    }
}
