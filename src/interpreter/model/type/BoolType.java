package interpreter.model.type;

import interpreter.model.values.BoolValue;
import interpreter.model.values.Value;

public non-sealed class BoolType implements Type {
    private static BoolType instance = null;
    private BoolType(){};
    public static BoolType get(){
        if (instance == null) {
            instance = new BoolType();
        }
        return instance;
    }
    @Override
    public boolean equals(Type other) {
        return other instanceof BoolType;
    }

    @Override
    public Value getDefault() {
        return new BoolValue(false);
    }


    @Override
    public TypeToken getToken() {
        return TypeToken.BOOLEAN;
    }
    @Override
    public String toString(){
        return "bool ";
    }
    @Override
    public Type deepCopy() {
        return get();
    }
}
