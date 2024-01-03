package interpreter.model.symboltable;

import interpreter.model.exceptions.ExpressionException;
import interpreter.model.exceptions.SymbolTableException;
import interpreter.model.utils.DeepCopiable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolTableHashMap<Identifier, Value extends DeepCopiable> implements SymbolTable<Identifier, Value> {
    private static class MyPair<T1 extends DeepCopiable, T2> implements DeepCopiable {
        private final T1 first;
        private final T2 second;

        MyPair(T1 f, T2 s) {
            first = f;
            second = s;
        }

        public T1 first() {
            return first;
        }

        public T2 second() {
            return second;
        }

        @Override
        public String toString() {
            return "(%s , %s)".formatted(first.toString(), second.toString());
        }

        @Override
        public MyPair<T1, T2> deepCopy() throws ExpressionException {
            return new MyPair<>((T1) first.deepCopy(), second);
        }
    }

    static final int MAX_SCOPE = 512, MIN_SCOPE = 0;
    final Map<Identifier, MyPair<Value, Integer>> storage;
    int currentScope;

    public SymbolTableHashMap() {
        this.storage = new HashMap<>();
        this.currentScope = 0;
    }

    private SymbolTableHashMap(Map<Identifier, MyPair<Value, Integer>> copiedStorage, int currentScope) {
        this.storage = copiedStorage;
        this.currentScope = currentScope;
    }

    @Override
    public void put(Identifier identifier, Value valueInformation) throws SymbolTableException {
        if (storage.containsKey(identifier))
            throw new SymbolTableException("Redeclaration of variable -- %s".formatted(identifier));
        storage.put(identifier, new MyPair<>(valueInformation, currentScope));
    }

    @Override
    public Value lookup(Identifier identifier) throws SymbolTableException {
        var aux = storage.get(identifier);
        if (aux == null)
            throw new SymbolTableException("Implicit declaration of variable -- %s ".formatted(identifier));
        return storage.get(identifier).first();
    }

    @Override
    public void update(Identifier identifier, Value value) throws SymbolTableException {
        if (!storage.containsKey(identifier))
            throw new SymbolTableException("Use of an undeclared variable -- %s".formatted(identifier.toString()));
        storage.put(identifier, new MyPair<>(value, storage.get(identifier).second()));
    }

    @Override
    public void incScope() throws SymbolTableException {
        if (currentScope >= MAX_SCOPE) {
            throw new SymbolTableException("Max scope depth exceeded -- %d/%d".formatted(currentScope, MAX_SCOPE));
        }
        ++currentScope;
    }

    @Override
    public void decScope() throws SymbolTableException {
        if (currentScope <= MIN_SCOPE) {
            throw new SymbolTableException("Minimum scope depth exceeded -- %d/%d".formatted(currentScope, MIN_SCOPE));
        }
        --currentScope;
    }

    @Override
    public void removeOutOfScopeVariables() {
        storage.keySet().removeIf(id -> storage.get(id).second() >= currentScope);
    }

    @Override
    public Collection<Value> getValues() {
        return storage
                .keySet()
                .stream()
                .map(key -> storage.get(key).first())
                .collect(Collectors.toList());
    }

    public SymbolTable<Identifier, Value> deepCopy() {
        Map<Identifier, MyPair<Value, Integer>> copiedStorage = storage
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> this.storage.get(entry.getKey())
                        )
                );
        return new SymbolTableHashMap<>(copiedStorage, currentScope);
    }

    @Override
    public Stream<Map.Entry<Identifier, Value>> stream() {
        return storage.entrySet().stream().map(entry -> new Map.Entry<>() {
            @Override
            public Identifier getKey() {
                return entry.getKey();
            }

            @Override
            public Value getValue() {
                return entry.getValue().first();
            }

            @Override
            public Value setValue(Value value) {
                return null;
            }
        });
    }


    @Override
    public String toString() {
        return storage.keySet().stream().
                map(key -> key.toString() + " == " + storage.get(key).first() + " ; ").
                collect(Collectors.joining());
    }


}
