package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.values.BoolValue;
import interpreter.model.values.Value;

public class WhileStatement implements Statement {
    final Expression condition;

    final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, HeapException, ProgramStateException {
        Value val = condition.evaluate(state);
        if (!(val instanceof BoolValue condVal)) {
            throw new StatementException("Condition of while statement is not evaluable to a boolean");
        }
        if (condVal.isTrue()) {
            var stack = state.getExecutionStack();
            stack.push(this);
            stack.push(new ScopeConclusionStatement());
            stack.push(body);
            state.incCurrentScope();
        }
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new WhileStatement(condition.deepCopy(), body.deepCopy());
    }

    @Override
    public String toString() {
        return "while(%s)(\n%s\n)".formatted(condition.toString(), body.toString());
    }

}
