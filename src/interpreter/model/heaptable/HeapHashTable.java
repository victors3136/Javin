package interpreter.model.heaptable;

import interpreter.model.exceptions.HeapException;
import interpreter.model.values.Value;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HeapHashTable implements HeapTable {
    private static final int HEAP_SIZE = 1024;
    private Map<Integer, Value> storage;

    public HeapHashTable() {
        storage = new HashMap<>();
    }

    public HeapHashTable(HashMap<Integer, Value> newHeap) {
        storage = newHeap;
    }

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
                iterate(1, i -> i < HEAP_SIZE, i -> i + 1).
                filter(i -> storage.get(i) == null).
                findFirst().
                orElse(0);
        if (index == 0)
            throw new HeapException("Heap overflow");
        storage.put(index, value);
        return index;
    }

    @Override
    public void update(int address, Value newValue) throws HeapException {
        if (storage.get(address) == null)
            throw new HeapException("Segmentation fault. Core dumped");
        storage.put(address, newValue);
    }

    @Override
    public void set(Map<Integer, Value> newHeapState) throws HeapException {
        if (newHeapState == null)
            throw new HeapException("Invalid overwrite of heap section");
        storage = newHeapState;
    }

    @Override
    public Stream<AbstractMap.Entry<Integer, Value>> entriesStream() {
        return storage.entrySet().stream();
    }

    @Override
    public void cleanup() {
        storage.clear();
    }


    @Override
    public String toString() {
        return storage.keySet().stream()
                .map(heap_id -> "%d: %s; ".formatted(heap_id, storage.get(heap_id)))
                .collect(Collectors.joining());
    }

}