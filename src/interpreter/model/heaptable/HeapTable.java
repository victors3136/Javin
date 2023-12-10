package interpreter.model.heaptable;

import interpreter.model.exceptions.HeapException;
import interpreter.model.values.Value;

import java.util.AbstractMap;
import java.util.stream.Stream;

public interface HeapTable {
    Value get(int index) throws HeapException;

    int add(Value value) throws HeapException;

    void update(int address, Value expr) throws HeapException;

    Stream<AbstractMap.Entry<Integer, Value>> entriesStream();

}