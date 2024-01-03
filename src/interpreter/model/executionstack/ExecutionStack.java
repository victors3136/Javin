package interpreter.model.executionstack;


import java.util.stream.Stream;

public interface ExecutionStack<T> {
    void push(T t);
    T pop();
    boolean empty();

    Stream<T> stream();
}
