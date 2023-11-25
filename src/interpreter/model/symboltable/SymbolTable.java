package interpreter.model.symboltable;


import interpreter.model.exceptions.SymbolTableException;

public interface SymbolTable<Identifier, Value, Scope extends Comparable<Scope>> {
    void put(Identifier identifier, Value value, Scope scope) throws SymbolTableException;
    Value lookup(Identifier identifier);
    void update(Identifier identifier, Value value) throws SymbolTableException;
    Scope scope(Identifier identifier) throws SymbolTableException;

    void removeOutOfScopeVariables(Scope currentScope);
}
