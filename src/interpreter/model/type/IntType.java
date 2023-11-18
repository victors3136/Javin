package interpreter.model.type;

import interpreter.model.values.IntValue;
import interpreter.model.values.Value;

public non-sealed class IntType implements Type{
    private static IntType instance;
    private IntType() {}

    public static IntType get() {
        if (instance == null) {
            instance = new IntType();
        }
        return instance;
    }
    @Override
    public boolean equals(Type other) {
        return other instanceof IntType;
    }

    @Override
    public Value getDefault() {
        return new IntValue(0);
    }

    @Override
    public TypeToken getToken() {
        return TypeToken.INTEGER;
    }

    @Override
    public Type deepCopy() {
        return get();
    }
    @Override
    public String toString(){
        return "int ";
    }
}
