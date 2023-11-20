package interpreter.model.statements;
import interpreter.model.exceptions.HeapException;
import interpreter.model.outputlist.OutputList;
import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.expressions.Expression;
import interpreter.model.exceptions.ValueException;
import interpreter.model.values.Value;

public class PrintStatement implements Statement {
    Expression expressionToPrint;
    public PrintStatement(Expression expressionToPrint){
        this.expressionToPrint = expressionToPrint;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ValueException, ExpressionException, HeapException {
        OutputList<Value> outputList  = state.getOutputList();
        if(outputList == null)
            return state;
        outputList.append(expressionToPrint.evaluate(state));
        return state;
    }

    @Override
    public Statement deepCopy() throws ExpressionException {
        return new PrintStatement(expressionToPrint.deepCopy());
    }

    @Override
    public String toString(){
        return "print( "+ expressionToPrint.toString()+" )";
    }

}
