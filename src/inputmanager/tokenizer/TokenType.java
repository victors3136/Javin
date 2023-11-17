package inputmanager.tokenizer;

public enum TokenType {
    TYPE_BOOL,
    TYPE_INT,
    TYPE_STR,
    CONST_BOOLEAN,
    CONST_INTEGER,
    CONST_STRING,
    KEYWORD_IF,
    KEYWORD_BRANCH,
    KEYWORD_PRINT,
    EXP_OP,
    MUL_DIV_OP,
    ADD_SUB_OP,
    ASSIGNMENT_OP,
    RELATIONAL_OP,
    LOGICAL_OP,
    OPEN_PARENTHESIS,
    CLOSED_PARENTHESIS,
    KEYWORD_COMPOUND,
    KEYWORD_OPEN_FILE,
    KEYWORD_READ_FILE,
    KEYWORD_CLOSE_FILE,
    IDENTIFIER,
    EMPTY;

    @Override
    public String toString() {
        return switch (this) {
            case TYPE_BOOL -> "bool";
            case TYPE_INT -> "int";
            case TYPE_STR -> "str";
            case CONST_BOOLEAN -> "boolean_constant";
            case CONST_INTEGER -> "integer_constant";
            case CONST_STRING -> "string_constant";
            case KEYWORD_IF -> "conditional";
            case KEYWORD_BRANCH -> "clauses";
            case KEYWORD_PRINT -> "print";
            case EXP_OP -> "exponential_operand";
            case MUL_DIV_OP -> "star_or_slash";
            case ADD_SUB_OP -> "plus_or_minus";
            case RELATIONAL_OP -> "relational_operand";
            case LOGICAL_OP -> "logical_operand";
            case ASSIGNMENT_OP -> "assign_operand";
            case OPEN_PARENTHESIS -> "(";
            case CLOSED_PARENTHESIS -> ")";
            case KEYWORD_COMPOUND -> "compound";
            case KEYWORD_OPEN_FILE -> "open_file";
            case KEYWORD_READ_FILE -> "read_file";
            case KEYWORD_CLOSE_FILE -> "close_file";
            case IDENTIFIER -> "variable_name";
            case EMPTY -> "∅";
        };
    }

    public String toRegex() {
        return switch (this) {
            case TYPE_BOOL -> "^bool";
            case TYPE_INT -> "^int";
            case TYPE_STR -> "^str";
            case CONST_BOOLEAN -> "^(true|false)";
            case CONST_INTEGER -> "^(-)?(0|([1-9][0-9]*))";
            case CONST_STRING -> "^\"[^\"]*\"";
            case KEYWORD_IF -> "^if";
            case KEYWORD_BRANCH -> "^else";
            case KEYWORD_PRINT -> "^print";
            case EXP_OP -> "^(\\^)";
            case MUL_DIV_OP -> "^((\\*)|(/))";
            case ADD_SUB_OP -> "^((\\+)|(-))";
            case RELATIONAL_OP -> "^((==)|(!=)|(<=)|(<)|(>=)|(>))";
            case LOGICAL_OP -> "^(\\&|\\|)";
            case ASSIGNMENT_OP -> "^(<-)";
            case OPEN_PARENTHESIS -> "^(\\()";
            case CLOSED_PARENTHESIS -> "^(\\))";
            case KEYWORD_COMPOUND -> "^;";
            case KEYWORD_OPEN_FILE -> "^fopen";
            case KEYWORD_READ_FILE -> "^fread";
            case KEYWORD_CLOSE_FILE -> "^fclose";
            case IDENTIFIER -> "^[a-zA-Z_][a-zA-Z_0-9]*";
            case EMPTY -> "^$";
        };
    }
    private int getPrecedence(){
        return switch (this){
            case EXP_OP -> 5;
            case MUL_DIV_OP -> 4;
            case ADD_SUB_OP -> 3;
            case RELATIONAL_OP -> 2;
            case LOGICAL_OP -> 1;
            case ASSIGNMENT_OP -> 0;
            default -> -1;
        };
    }

    public boolean greaterOrEqualPrecedence(TokenType type) {
        return this.getPrecedence() >= type.getPrecedence();
    }
}
