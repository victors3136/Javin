package interpreter.model.heapmanager;

import interpreter.model.exceptions.HeapException;
import interpreter.model.values.Value;

import java.util.Map;

public interface HeapManager {
    Value get(int index) throws HeapException;

    int add(Value value) throws HeapException;

    void update(int address, Value expr) throws HeapException;

    void set(Map<Integer, Value> newHeapState) throws HeapException;
}