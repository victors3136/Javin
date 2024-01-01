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

public class HeapWriteStatement implements Statement {
    final String identifier;
    final Expression valueExpr;
    public HeapWriteStatement(String identifier, Expression valueExpr){
        this.identifier = identifier;
        this.valueExpr = valueExpr;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, HeapException, SymbolTableException {
        ReferenceValue ref = (ReferenceValue) state.getSymbolTable().lookup(identifier);
        Value expr = valueExpr.evaluate(state);
        state.getHeapTable().update(ref.getAddress(), expr);
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
        if(varType == null)
            throw new TypecheckException("Use of undeclared identifier -- %s".formatted(identifier));
        //noinspection DuplicatedCode
        if (!(varType instanceof ReferenceType ref)) {
            throw new TypecheckException("Variable does not reference the heap -- %s".formatted(varType));
        }
        Type expType = valueExpr.typecheck(environment);
        if (ref.getInner() != expType) {
            throw new TypecheckException("Source and destination do not have the same type -- (%s %s)".formatted(ref.getInner(), expType));
        }
        return environment;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new HeapWriteStatement(identifier, valueExpr.deepCopy());
    }

    @Override
    public String toString(){
        return "heap_write(%s, %s)".formatted(identifier, valueExpr.toString());
    }
}
