package interpreter.model.statements.memorystatements;

import interpreter.model.exceptions.*;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.ReferenceType;
import interpreter.model.type.Type;
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
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, SymbolTableException, HeapException {
    //        ReferenceValue refVal = (ReferenceValue) state.getSymbolTable().lookup(identifier);
        Value expr = expression.evaluate(state);
        int address = state.getHeapTable().add(expr);
        state.getSymbolTable().update(identifier, new ReferenceValue(address, expr.getType()));
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type varType;
        try {
            varType = environment.lookup(identifier);
        } catch (SymbolTableException ste) {
            throw new TypecheckException(ste.getMessage());
        }
        if (varType == null) {
            throw new TypecheckException("Undeclared reference variable -- %s".formatted(identifier));
        }
        //noinspection DuplicatedCode
        if (!(varType instanceof ReferenceType ref)) {
            throw new TypecheckException("Variable does not reference the heap -- %s".formatted(varType));
        }
        Type expType = expression.typecheck(environment);
        if (ref.getInner() != expType) {
            throw new TypecheckException("Source and destination do not have the same type -- (%s %s)".formatted(ref.getInner(), expType));
        }
        return environment;
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