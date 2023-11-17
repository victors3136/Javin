package interpreter.model.heapmanager;

import interpreter.model.exceptions.HeapException;
import interpreter.model.values.Value;

import java.util.Map;
import java.util.stream.IntStream;

public class HeapManagerTable implements HeapManager {
    Map<Integer, Value> storage;
    @Override
    public Value get(int index) throws HeapException {

        Value value = storage.get(index);
        if (value == null) {
            throw new HeapException("Segmentation fault. Core dumped");
        }
        return value;
    }

    @Override
    public int add(Value value) throws HeapException {
        int index = IntStream.
                iterate(1, i -> i + 1).
                filter(i -> storage.get(i) == null).
                findFirst().
                orElse(0);
        if (index == 0)
            throw new HeapException("Heap overflow");
        storage.put(index, value);
        return index;
    }
}
