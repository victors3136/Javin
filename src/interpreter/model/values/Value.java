package interpreter.model.values;

import interpreter.model.type.Type;
import interpreter.model.utils.DeepCopiable;

public interface Value extends DeepCopiable {
    Type getType();

    String toString();

    Value deepCopy();
}
