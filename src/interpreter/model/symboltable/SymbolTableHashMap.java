package interpreter.model.symboltable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import interpreter.model.exceptions.SymbolTableException;

public class SymbolTableHashMap<K, V> implements SymbolTable<K, V> {

    Map<K, V> storage;

    public SymbolTableHashMap() {
        this.storage = new HashMap<>();
    }

    @Override
    public void put(K identifier, V value) throws SymbolTableException {
        if (storage.containsKey(identifier))
            throw new SymbolTableException("Redeclaration of a variable --" + identifier.toString());
        storage.put(identifier, value);
    }

    @Override
    public V lookup(K identifier) {
        return storage.get(identifier);
    }

    @Override
    public void update(K identifier, V value) throws SymbolTableException {
        if (!storage.containsKey(identifier))
            throw new SymbolTableException("Use of an undeclared variable -- " + identifier.toString());
        storage.put(identifier, value);
    }

    @Override
    public String toString() {
        return storage.keySet().stream().
                map(key -> key.toString() + " == " + storage.get(key) + "\n").
                collect(Collectors.joining());
    }
}
