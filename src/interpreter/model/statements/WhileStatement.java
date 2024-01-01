package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.BoolType;
import interpreter.model.type.Type;
import interpreter.model.values.BoolValue;

public class WhileStatement implements Statement {
    final Expression condition;

    final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, HeapException, SymbolTableException {
        BoolValue condVal = (BoolValue) condition.evaluate(state);
        if (condVal.isTrue()) {
            var stack = state.getExecutionStack();
            stack.push(this);
            stack.push(new ScopeConclusionStatement());
            stack.push(body);
            state.getSymbolTable().incScope();
        }
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type type = condition.typecheck(environment);
        if (!(type instanceof BoolType))
            throw new TypecheckException("Condition inside 'while' statement does not eval to a boolean -- instead %s evaluates to a %s".formatted(condition,type));
        body.typecheck(environment.deepCopy()); /// WHY???
        return environment;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new WhileStatement(condition.deepCopy(), body.deepCopy());
    }

    @Override
    public String toString() {
        return "while( %s )( %s )".formatted(condition.toString(), body.toString());
    }

}
