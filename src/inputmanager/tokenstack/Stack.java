package inputmanager.tokenstack;
import java.util.EmptyStackException;

public interface Stack<T> {
    Stack<T> push(T t);
    boolean isEmpty();
    T top() throws EmptyStackException;
    T pop() throws EmptyStackException;
    void clear();
    String toString();
}
