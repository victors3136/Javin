package interpreter.model.statements.memorystatements;

import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.HeapException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.values.ReferenceValue;
import interpreter.model.values.Value;

public class HeapWriteStatement implements Statement {
    String identifier;
    Expression valueExpr;
    public HeapWriteStatement(String identifier, Expression valueExpr){
        this.identifier = identifier;
        this.valueExpr = valueExpr;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, HeapException {
        Value val = state.getSymbolTable().lookup(identifier);
        if(val == null)
            throw new ExpressionException("Use of undeclared identifier -- %s".formatted(identifier));
        if(!(val instanceof ReferenceValue ref)){
            throw new ExpressionException("Identifier does not name a reference -- %s".formatted(identifier));
        }
        Value expr = valueExpr.evaluate(state);
        if(! ref.getType().equals(expr.getType())) {
            throw new ExpressionException("");
        }
        state.getHeapManager().update(ref.getAddress(), expr);
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new HeapWriteStatement(identifier, valueExpr.deepCopy());
    }
}
