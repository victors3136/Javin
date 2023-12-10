package interpreter.model.outputlist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public String toString() {
        return storage.stream().
                map(element -> element + "\n").
                collect(Collectors.joining());
    }
}
