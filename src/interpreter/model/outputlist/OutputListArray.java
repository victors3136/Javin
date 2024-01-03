package interpreter.model.outputlist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OutputListArray<T> implements OutputList<T> {
    private final List<T> storage;

    public OutputListArray() {
        this.storage = new ArrayList<>();
    }

    @Override
    public void append(T t) {
        storage.add(t);
        System.out.println(t);
    }

    @Override
    public Stream<T> stream() {
        return storage.stream();
    }

    @Override
    public String toString() {
        return storage.stream().
                map(element -> element + "\n").
                collect(Collectors.joining());
    }
}
