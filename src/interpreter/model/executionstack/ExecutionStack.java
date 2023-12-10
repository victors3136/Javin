package interpreter.model.executionstack;


public interface ExecutionStack<T> {
    void push(T t);
    T pop();
    boolean empty();
}
