package interpreter.model.heapmanager;
import interpreter.model.exceptions.HeapException;
import interpreter.model.values.Value;

public interface HeapManager {
    Value get(int index) throws HeapException;
    int add(Value value) throws HeapException;
}
