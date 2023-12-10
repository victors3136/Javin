package interpreter.model.symboltable;


import interpreter.model.exceptions.SymbolTableException;

import java.util.Collection;

public interface SymbolTable<Identifier, Value> {
    void put(Identifier identifier, Value value) throws SymbolTableException;
    Value lookup(Identifier identifier) throws SymbolTableException;
    void update(Identifier identifier, Value value) throws SymbolTableException;
    void incScope() throws SymbolTableException;
    void decScope() throws SymbolTableException;

    void removeOutOfScopeVariables();

    Collection<Value> getValues();

    SymbolTable<Identifier,Value> deepCopy();
}
