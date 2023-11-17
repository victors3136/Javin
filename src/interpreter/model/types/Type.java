package interpreter.model.types;

import interpreter.model.operands.Operand;
import interpreter.model.exceptions.TypeException;
import interpreter.model.values.*;

public enum Type {
    NULL,
    BOOLEAN,
    INTEGER,
    STRING,
    REFERENCE;

    @Override
    public String toString(){
        return switch(this){
            case BOOLEAN -> "bool ";
            case INTEGER -> "int ";
            case STRING -> "string ";
            case REFERENCE -> "ref ";
            case NULL -> "default ";
        };
    }
    private boolean additive() {return this == INTEGER || this == STRING; }

    private boolean numeric(){
        return this == INTEGER;
    }
    private boolean testableForEquality(){
        return this == INTEGER || this == BOOLEAN;
    }
    private boolean comparable(){
        return this == INTEGER;
    }
    private boolean booleable() {
        return this == BOOLEAN;
    }
    public final boolean isSuitableFor(Operand operand){
        return
                switch(operand){
                    case ADD-> this.additive();
                    case EXP, DIV, MUL,SUB -> this.numeric();
                    case AND , OR -> this.booleable();
                    case EQUAL, NOT_EQUAL -> this.testableForEquality();
                    case LOWER, LOWER_OR_EQUAL, GREATER, GREATER_OR_EQUAL -> this.comparable();
                };
    }

    public Value instantiateDefault() throws TypeException {
        return
                switch (this){
                    case BOOLEAN -> new BoolValue();
                    case INTEGER -> new IntValue();
                    case STRING -> new StringValue();
//                    case REFERENCE -> new ReferenceValue(0,null);
                    default -> throw new TypeException("You tried to instantiate NULL...");
                };
    }
    public Type deepCopy(){
        return this;
    }
}
