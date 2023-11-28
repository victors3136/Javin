package interpreter.model.expressions;

import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.HeapException;
import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.programstate.ProgramState;
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
    public Expression deepCopy() throws ExpressionException {
        return new HeapReadExpression(offsetSpecificationFormula.deepCopy());
    }

    @Override
    public String toString() {
        return "heap_read(%s)".formatted(offsetSpecificationFormula.toString());
    }
}
