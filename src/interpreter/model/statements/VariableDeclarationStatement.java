package interpreter.model.statements;
import interpreter.model.programstate.ProgramState;
import interpreter.model.symboltable.SymbolTable;
import interpreter.model.type.Type;
import interpreter.model.exceptions.StatementException;
import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.exceptions.TypeException;
import interpreter.model.values.Value;

public class VariableDeclarationStatement implements Statement {
    final String identifier;
    final Type type;
    public VariableDeclarationStatement(String id, Type type ){
        identifier = id;
        this.type = type;
    }
    public VariableDeclarationStatement(Type t , String id){
        type = t;
        identifier = id;
    }
    @Override
    public ProgramState execute(ProgramState state) throws StatementException, SymbolTableException {
        SymbolTable<String, Value, Integer> st = state.getSymbolTable();
        if(st.lookup(identifier) != null)
            throw new StatementException("Redeclaration of a variable");
        st.put(identifier, type.getDefault(), state.getCurrentScope());
        return state;
    }

    @Override
    public Statement deepCopy() {
        return new VariableDeclarationStatement(identifier, type.deepCopy());
    }

    @Override
    public String toString(){
        return type.toString()+  " " + identifier;
    }
}
