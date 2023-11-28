package interpreter.model.type;

import interpreter.model.values.StringValue;
import interpreter.model.values.Value;

public non-sealed class StringType implements Type{
    private static StringType instance = null;
    private StringType(){}

    public static StringType get(){
        if(instance == null)
            instance = new StringType();
        return instance;
    }
    @Override
    public boolean equals(Type other) {
        return other instanceof StringValue;
    }

    @Override
    public Value getDefault() {
        return new StringValue("");
    }


    @Override
    public TypeToken getToken() {
        return TypeToken.STRING;
    }

    @Override
    public Type deepCopy() {
        return get();
    }
    @Override
    public String toString(){
        return "string ";
    }
}
