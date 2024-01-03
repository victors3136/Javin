package interpreter.model.outputlist;
import java.util.stream.Stream;

public interface OutputList<T> {
    void append(T t);

    Stream<T> stream();
}
