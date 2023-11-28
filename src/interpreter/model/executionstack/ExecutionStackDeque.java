package interpreter.model.executionstack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

public class ExecutionStackDeque<T> implements ExecutionStack<T> {

    final Deque<T> storage;

    public ExecutionStackDeque() {
        storage = new ArrayDeque<>();
    }

    public ExecutionStackDeque(T t) {
        storage = new ArrayDeque<>();
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
                map(elem -> elem + " | ").
                collect(Collectors.joining());
    }
}
