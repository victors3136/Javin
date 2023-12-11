package interpreter.model.statements;

import interpreter.model.exceptions.*;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
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
        state.getSymbolTable().update(variableIdentifier, rightHandSide);
        return null;
    }

    @Override
    public SymbolTable<String, Type> typecheck(SymbolTable<String, Type> environment) throws TypecheckException {
        Type varType;
        try {
            varType = environment.lookup(variableIdentifier);
        } catch (SymbolTableException ste) {
            throw new TypecheckException(ste.getMessage());
        }
        Type expType = expressionAssignedToVar.typecheck(environment);
        if (varType == null)
            throw new TypecheckException("Implicit declaration of a variable -- %s".formatted(variableIdentifier));
        if(!varType.equals(expType)){
            throw new TypecheckException("Assignment -- mismatched lhs type (%s) and rhs type (%s)".formatted(varType, expType));
        }
        return environment;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new AssignStatement(variableIdentifier, expressionAssignedToVar.deepCopy());
    }

    public String toString() {
        return variableIdentifier + " <- " + expressionAssignedToVar.toString();
    }
}
