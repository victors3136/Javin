package interpreter.model.type;


import interpreter.model.utils.DeepCopiable;
import interpreter.model.values.Value;

public sealed interface Type extends DeepCopiable permits BoolType, IntType, ReferenceType, StringType {
    boolean equals(Type other);
    Value getDefault();
    String toString();
    TypeToken getToken();
    Type deepCopy();
}