package interpreter.model.executionstack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

public class ExecutionStackDeque<T> implements ExecutionStack<T> {

    final Deque<T> storage = new ArrayDeque<>();

    public ExecutionStackDeque() {
    }

    public ExecutionStackDeque(T t) {
        this.storage.push(t);
    }

    @Override
    public void push(T t) {
        storage.push(t);
    }

    @Override
    public T pop() {
        return storage.pop();
    }

    @Override
    public T top() {
        return storage.peek();
    }

    @Override
    public boolean empty() {
        return storage.isEmpty();
    }

    @Override
    public String toString() {
        return storage.stream().
                map(elem -> elem + "\n┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉\n").
                collect(Collectors.joining())+"#################\n";
    }
}
