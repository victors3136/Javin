package interpreter.model.statements;
import interpreter.model.expressions.Expression;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.types.Type;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.StatementException;
import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.ValueException;
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
        if(dictionary==null)
            return state;
        Value valueOfExpression = expressionAssignedToVar.evaluate(dictionary);
        Type variablePreassignedType = dictionary.lookup(variableIdentifier).getType();
        if ( ! valueOfExpression.isOfType(variablePreassignedType))
            throw new StatementException("Unmatched value-type combination -- " + valueOfExpression + " and " + variablePreassignedType );
        dictionary.update(variableIdentifier, valueOfExpression);
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new AssignStatement(new String(variableIdentifier), expressionAssignedToVar.deepCopy());
    }

    public String toString(){
        return variableIdentifier + " <- " + expressionAssignedToVar.toString();
    }
}
