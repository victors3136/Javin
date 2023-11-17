package interpreter.model.symboltable;


import interpreter.model.exceptions.SymbolTableException;

public interface SymbolTable<K,V> {
    void put(K identifier, V value) throws SymbolTableException;
    V lookup(K identifier);
    void update(K identifier, V value) throws SymbolTableException;
}
