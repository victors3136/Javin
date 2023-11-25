package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.type.Type;
import interpreter.model.values.Value;

public class AssignStatement implements Statement {

    final String variableIdentifier;
    final Expression expressionAssignedToVar;

    public AssignStatement(String variableIdentifier, Expression expressionAssignedToVar) {
        this.variableIdentifier = variableIdentifier;
        this.expressionAssignedToVar = expressionAssignedToVar;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, StatementException, SymbolTableException, HeapException {
        Value rightHandSide = expressionAssignedToVar.evaluate(state);
        Value leftHandSide = state.getSymbolTable().lookup(variableIdentifier);
        if (leftHandSide == null)
            throw new StatementException("Implicit declaration of a variable -- " + variableIdentifier);
        Type variablePreassignedType = leftHandSide.getType();
        if (!rightHandSide.getType().equals(variablePreassignedType))
            throw new StatementException("Unmatched value-type combination -- " + rightHandSide + " and " + variablePreassignedType);
        state.getSymbolTable().update(variableIdentifier, rightHandSide);
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new AssignStatement(variableIdentifier, expressionAssignedToVar.deepCopy());
    }

    public String toString() {
        return variableIdentifier + " <- " + expressionAssignedToVar.toString();
    }
}
