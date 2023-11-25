package interpreter.model.symboltable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import interpreter.model.exceptions.SymbolTableException;

public class SymbolTableHashMap<Identifier, Value, Scope extends Comparable<Scope> > implements SymbolTable<Identifier, Value, Scope> {
    final Map<Identifier, Value> values;
    final Map<Identifier, Scope> scopes;

    public SymbolTableHashMap() {
        this.values = new HashMap<>();
        this.scopes = new HashMap<>();
    }

    @Override
    public void put(Identifier identifier, Value valueInformation, Scope scope) throws SymbolTableException {
        if (values.containsKey(identifier)|| scopes.containsKey(identifier))
            throw new SymbolTableException("Redeclaration of a variable --" + identifier.toString());
        values.put(identifier, valueInformation);
        scopes.put(identifier, scope);
    }

    @Override
    public Value lookup(Identifier identifier) {
        return values.get(identifier);
    }

    @Override
    public void update(Identifier identifier, Value value) throws SymbolTableException {
        if (!values.containsKey(identifier))
            throw new SymbolTableException("Use of an undeclared variable -- %s".formatted(identifier.toString()));
        values.put(identifier, value);
    }

    @Override
    public Scope scope(Identifier identifier) throws SymbolTableException {
        Scope result = scopes.get(identifier);
        if(result == null)
            throw new SymbolTableException("Use of an undeclared variable -- %s".formatted(identifier.toString()));
        return result;
    }

    @Override
    public void removeOutOfScopeVariables(Scope currentScope) {
        Iterator<Identifier> iterator = scopes.keySet().iterator();
        while(iterator.hasNext()){
            Identifier id = iterator.next();
            if(scopes.get(id).compareTo(currentScope)>=0){
                iterator.remove();
                values.remove(id);
            }
        }
    }

    @Override
    public String toString() {
        return values.keySet().stream().
                map(key -> key.toString() + " == " + values.get(key) + "\n").
                collect(Collectors.joining());
    }
}
