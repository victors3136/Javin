package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.BoolType;
import interpreter.model.type.Type;
import interpreter.model.values.BoolValue;

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
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, HeapException, SymbolTableException {
        ExecutionStack<Statement> stack = state.getExecutionStack();
        if (stack == null)
            return state;
        BoolValue conditionValue = (BoolValue) condition.evaluate(state);
        stack.push(new ScopeConclusionStatement());
        if (conditionValue.isTrue())
            stack.push(branchPositive);
        else
            stack.push(branchNegative);
        state.getSymbolTable().incScope();
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type type = condition.typecheck(environment);
        if (!(type instanceof BoolType))
            throw new TypecheckException("Condition inside 'if' statement does not eval to a boolean -- instead %s evaluates to %s".formatted(condition, type));
        branchPositive.typecheck(environment.deepCopy()); /// WHY???
        branchNegative.typecheck(environment.deepCopy());
        return environment;
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
