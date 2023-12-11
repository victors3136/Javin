package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.ReferenceType;
import interpreter.model.type.Type;
import interpreter.model.values.ReferenceValue;
import interpreter.model.values.Value;

public class HeapReadExpression implements Expression {
    final Expression offsetSpecificationExpression;

    public HeapReadExpression(Expression offsetSpecificationExpression) {
        this.offsetSpecificationExpression = offsetSpecificationExpression;
    }

    @Override
    public Value evaluate(ProgramState state) throws ValueException, ExpressionException, HeapException, SymbolTableException {
        ReferenceValue refVal = (ReferenceValue) offsetSpecificationExpression.evaluate(state);
        return state.getHeapTable().get(refVal.getAddress());
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type type = offsetSpecificationExpression.typecheck(environment);
        if(! (type instanceof ReferenceType ref)){
            throw new TypecheckException("Offset specification expression does not evaluate to a reference type-- %s".formatted(type));
        }
        return ref.getInner();
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new HeapReadExpression(offsetSpecificationExpression.deepCopy());
    }

    @Override
    public String toString() {
        return "heap_read(%s)".formatted(offsetSpecificationExpression.toString());
    }
}
