package interpreter.model.filetable;

import interpreter.model.values.StringValue;

import java.io.BufferedReader;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FileTableMap implements FileTable {
    final SortedMap<String, BufferedReader> storage;

    public FileTableMap() {
        super();
        storage = new TreeMap<>();
    }

    @Override
    public String toString() {
        return storage.keySet().stream()
                .map(string -> string + "\n")
                .collect(Collectors.joining());
    }

    @Override
    public void add(StringValue name, BufferedReader fileDescriptor) {
        storage.put(name.getValue(), fileDescriptor);
    }

    @Override
    public BufferedReader lookup(String value) {
        return storage.get(value);
    }

    @Override
    public void remove(String fileIdentifier) {
        storage.remove(fileIdentifier);
    }
}
