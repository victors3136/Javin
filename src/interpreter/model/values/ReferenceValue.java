//package interpreter.model.values;
//import interpreter.model.types.Type;
//import interpreter.model.exceptions.ValueException;
//
//public class ReferenceValue extends Value {
//
//    int address;
//    Type refType;
//
//    public ReferenceValue(int address,Type refType) {
//        this.address = address;
//        this.refType = refType;
//    }
//
//    @Override
//    public Type getType() {
//        return Type.REFERENCE;
//    }
//
//    @Override
//    public boolean isOfType(Type t) {
//        return t == Type.REFERENCE;
//    }
//
//    @Override
//    public String toString() {
//        return "ref (" + refType.toString() + ") at address "+address;
//    }
//
//    @Override
//    public Value deepCopy() {
//        return new ReferenceValue(address, refType.deepCopy());
//    }
//
//    @Override
//    public Value equal(Value other) throws ValueException {
//        if (!other.isOfType(Type.REFERENCE))
//            return new BoolValue(false);
//        ReferenceValue rv = (ReferenceValue) other;
//        return new BoolValue(this.refType== rv.refType);
//    }
//    public Type getInner(){
//        return refType;
//    }
//}
