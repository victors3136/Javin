//package interpreter.model.statements.memorystatements;
//
//import interpreter.model.exceptions.*;
//import interpreter.model.expressions.Expression;
//import interpreter.model.programstate.ProgramState;
//import interpreter.model.statements.Statement;
//import interpreter.model.types.Type;
//import interpreter.model.values.ReferenceValue;
//import interpreter.model.values.Value;
//
//public class HeapAllocationStatement implements Statement {
//    String identifier;
//    Expression expression;
//
//    HeapAllocationStatement(String identifier, Expression expression) {
//        this.identifier = identifier;
//        this.expression = expression;
//    }
//
//    @Override
//    public ProgramState execute(ProgramState state) throws StatementException, ValueException, ExpressionException, SymbolTableException, TypeException {
//        Value capture = state.getSymbolTable().lookup(identifier);
//        if (capture == null) {
//            throw new StatementException(":-(");
//        }
//        if (!capture.isOfType(Type.REFERENCE)) {
//            throw new StatementException(":--(");
//        }
//        ReferenceValue ref = (ReferenceValue)capture;
//        if(!ref.getInner().equals()
//        return state;
//    }
//
//    @Override
//    public String toString() {
//        return "new(" + identifier + ";" + expression.toString() + ") ";
//    }
//
//    @Override
//    public Statement deepCopy() throws ExpressionException {
//        return null;
//    }
//}
