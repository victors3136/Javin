package interpreter.model.statements;

import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.StatementException;
import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.ValueException;
import interpreter.model.type.Type;
import interpreter.model.values.Value;

public class AssignStatement implements Statement {

    String variableIdentifier;
    Expression expressionAssignedToVar;

    public AssignStatement(String variableIdentifier, Expression expressionAssignedToVar) {
        this.variableIdentifier = variableIdentifier;
        this.expressionAssignedToVar = expressionAssignedToVar;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, StatementException, SymbolTableException {
        SymbolTable<String, Value> dictionary = state.getSymbolTable();
        if (dictionary == null)
            return state;
        Value rightHandSide = expressionAssignedToVar.evaluate(dictionary);
        Value leftHandSide = dictionary.lookup(variableIdentifier);
        if (leftHandSide == null)
            throw new StatementException("Implicit declaration of a variable -- " + variableIdentifier);
        Type variablePreassignedType = leftHandSide.getType();
        if (!rightHandSide.getType().equals(variablePreassignedType))
            throw new StatementException("Unmatched value-type combination -- " + rightHandSide + " and " + variablePreassignedType);
        dictionary.update(variableIdentifier, rightHandSide);
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
