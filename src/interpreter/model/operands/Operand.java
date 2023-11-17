package interpreter.model.operands;

public enum Operand {
    ADD,
    SUB,
    MUL,
    DIV,
    EXP,
    AND,
    OR,
    EQUAL,
    NOT_EQUAL,
    LOWER,
    GREATER,
    LOWER_OR_EQUAL,
    GREATER_OR_EQUAL;

    @Override
    public String toString(){
        return switch(this){
            case ADD -> " + ";
            case SUB -> " - ";
            case MUL -> " * ";
            case DIV -> " / ";
            case EXP -> " ^ ";

            case AND -> " & ";
            case OR -> " | ";

            case EQUAL -> " == ";
            case NOT_EQUAL -> " != ";
            case LOWER -> " < ";
            case GREATER -> " > ";
            case LOWER_OR_EQUAL -> " <= ";
            case GREATER_OR_EQUAL -> " >= ";
        };
    }

    public boolean logical() {
        return
                switch (this){
                    case OR, AND -> true;
                    default -> false;
                };
    }
    public boolean arithmetic(){
        return
                switch(this){
                    case ADD, SUB, MUL, DIV, EXP -> true;
                    default -> false;
                };
    }
    public boolean relational(){
        return
                switch(this){
                    case EQUAL, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LOWER, LOWER_OR_EQUAL -> true;
                    default -> false;
                };
    }

    public Operand deepCopy() {
        return this;
    }
}
