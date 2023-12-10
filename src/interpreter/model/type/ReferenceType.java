package interpreter.model.type;

import interpreter.model.values.ReferenceValue;
import interpreter.model.values.Value;

import java.util.HashMap;
import java.util.Map;

public non-sealed class ReferenceType implements Type{
    final Type inner;
    final static Map<Type,ReferenceType> instances = new HashMap<>();

    private ReferenceType(Type t){ inner = t; }
    static public Type get(Type t){
        instances.computeIfAbsent(t, ReferenceType::new);
        return instances.get(t);
    }

    @Override
    public boolean equals(Type other) {
        if(! (other instanceof ReferenceType ref))
            return false;
        return this.inner.equals(ref.inner);
    }

    @Override
    public Value getDefault() {
        return new ReferenceValue(0, inner);
    }
    @Override
    public TypeToken getToken() {
        return TypeToken.REFERENCE;
    }

    @Override
    public Type deepCopy() {
        return get(inner);
    }
    @Override
    public String toString(){
        return "ref "+ inner.toString();
    }
}
