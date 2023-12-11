package interpreter.model.expressions;

import interpreter.model.exceptions.*;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.ReferenceType;
import interpreter.model.type.Type;
import interpreter.model.values.ReferenceValue;
import interpreter.model.values.Value;

public class HeapReadExpression implements Expression {
    final Expression offsetSpecificationFormula;

    public HeapReadExpression(Expression offsetSpecificationFormula) {
        this.offsetSpecificationFormula = offsetSpecificationFormula;
    }

    @Override
    public Value evaluate(ProgramState state) throws ValueException, ExpressionException, HeapException, SymbolTableException {
        Value val = offsetSpecificationFormula.evaluate(state);
        if (val instanceof ReferenceValue refVal)
            return state.getHeapTable().get(refVal.getAddress());
        throw new ExpressionException("Expression does not evaluate to a ref type -- %s".formatted(offsetSpecificationFormula.toString()));
    }

    @Override
    public Type typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type type = offsetSpecificationFormula.typecheck(environment);
        if(! (type instanceof ReferenceType ref)){
            throw new TypecheckException("Offset specification expression does not evaluate to a reference type-- %s".formatted(type));
        }
        return ref.getInner();
    }

    @Override
    public Expression deepCopy() throws ExpressionException {
        return new HeapReadExpression(offsetSpecificationFormula.deepCopy());
    }

    @Override
    public String toString() {
        return "heap_read(%s)".formatted(offsetSpecificationFormula.toString());
    }
}
