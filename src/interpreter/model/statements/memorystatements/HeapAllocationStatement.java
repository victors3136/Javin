package interpreter.model.statements.memorystatements;

import interpreter.model.exceptions.*;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.type.ReferenceType;
import interpreter.model.values.ReferenceValue;
import interpreter.model.values.Value;

public class HeapAllocationStatement implements Statement {
    final String identifier;
    final Expression expression;

    public HeapAllocationStatement(String identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, HeapException {
        Value value = state.getSymbolTable().lookup(identifier);

        if (value == null) {
            throw new StatementException("Undeclared reference variable -- %s".formatted(identifier));
        }
        if (!(value instanceof ReferenceValue refVal))
            throw new StatementException("Assigning a reference to a non reference type -- %s".formatted(value.getType()));
        Value expr = expression.evaluate(state);

        if (!refVal.getType().equals(ReferenceType.get(expr.getType())))
            throw new StatementException("Mismatched types -- %s, %s".formatted(refVal.getType(), ReferenceType.get(expr.getType())));
        int address = state.getHeapTable().add(expr);
        state.getSymbolTable().update(identifier, new ReferenceValue(address, expr.getType()));
        return null;
    }

    @Override
    public String toString() {
        return "heap_alloc ( %s , %s ) ".formatted(identifier, expression.toString());
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new HeapAllocationStatement(identifier, expression.deepCopy());
    }
}