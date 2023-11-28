package inputmanager.parser;

import inputmanager.tokenizer.Token;
import inputmanager.tokenizer.TokenType;
import inputmanager.tokenstack.TokenStack;
import interpreter.model.expressions.*;
import interpreter.model.operands.Operand;
import interpreter.model.statements.*;
import interpreter.model.statements.filestatements.CloseFileStatement;
import interpreter.model.statements.filestatements.OpenReadFileStatement;
import interpreter.model.statements.filestatements.ReadFileStatement;
import interpreter.model.exceptions.ExpressionException;
import interpreter.model.statements.memorystatements.HeapAllocationStatement;
import interpreter.model.statements.memorystatements.HeapWriteStatement;
import interpreter.model.type.*;
import interpreter.model.values.BoolValue;
import interpreter.model.values.IntValue;
import interpreter.model.values.StringValue;
import interpreter.model.values.Value;

import java.util.Objects;

public class RecursiveDescentParser implements Parser {
    TokenStack tokens;
    Token lookahead;

    private void next() {
        if (tokens.isEmpty())
            this.lookahead = null;
        else this.lookahead = tokens.pop();
    }

    public Statement program(TokenStack tokens) throws ParseException, ExpressionException {
        this.tokens = tokens;
        this.lookahead = this.tokens.pop();
        return statement();
    }

    private Statement statement() throws ParseException, ExpressionException {
        if (this.lookahead == null)
            return new NoOperationStatement();
        switch (this.lookahead.getType()) {
            case KEYWORD_COMPOUND -> {
                next();
                return new CompoundStatement(
                        statement(),
                        statement()
                );
            }
            case KEYWORD_HEAP_ALLOC -> {
                next();
                return new HeapAllocationStatement(identifier(), expression());
            }
            case KEYWORD_HEAP_WRITE -> {
                next();
                return new HeapWriteStatement(identifier(), expression());
            }
            case KEYWORD_IF -> {
                next();
                Expression condition = expression();
                if (this.lookahead.getType() == TokenType.KEYWORD_BRANCH) {
                    next();
                    return new IfStatement(condition, statement(), statement());
                } else {
                    return new IfStatement(condition, statement(), new NoOperationStatement());
                }
            }
            case KEYWORD_PRINT -> {
                next();
                return new PrintStatement(expression());
            }
            case KEYWORD_WHILE -> {
                next();
                return new WhileStatement(expression(), statement());
            }
            case EMPTY -> {
                next();
                return new NoOperationStatement();
            }
            case ASSIGNMENT_OP -> {
                next();
                return new AssignStatement(identifier(), expression());
            }
            case TYPE_BOOL, TYPE_INT, TYPE_STR, TYPE_REF -> {
                return new VariableDeclarationStatement(type(), identifier());
            }
            case KEYWORD_OPEN_FILE -> {
                next();
                return new OpenReadFileStatement(expression());
            }
            case KEYWORD_READ_FILE -> {
                next();
                return new ReadFileStatement(expression(), identifier());
            }
            case KEYWORD_CLOSE_FILE -> {
                next();
                return new CloseFileStatement(expression());
            }
            default ->
                    throw new ParseException("Invalid token for Statement -- " + this.lookahead.getSequence() + " " + this.lookahead.getType());
        }
    }

    private String identifier() {
        String id = this.lookahead.getSequence();
        next();
        return id;
    }

    private Expression expression() throws ParseException, ExpressionException {
        switch (this.lookahead.getType()) {
            case EXP_OP, MUL_DIV_OP, ADD_SUB_OP -> {
                return new ArithmeticExpression(arithmeticOperand(), expression(), expression());
            }
            case KEYWORD_HEAP_READ -> {
                next();
                return new HeapReadExpression(expression());
            }
            case LOGICAL_OP -> {
                return new LogicExpression(logicalOperand(), expression(), expression());
            }
            case RELATIONAL_OP -> {
                return new RelationalExpression(relationalOperand(), expression(), expression());
            }
            case CONST_BOOLEAN, CONST_INTEGER, CONST_STRING -> {
                return new ValueExpression(value());
            }
            case IDENTIFIER -> {
                return new VariableExpression(identifier());
            }
            default -> throw new ParseException("Invalid token for Expression -- " + this.lookahead.getSequence());
        }
    }

    private Type type() throws ParseException {
        Token prev = this.lookahead;
        next();
        return switch (prev.getType()) {
            case TYPE_INT -> IntType.get();
            case TYPE_BOOL -> BoolType.get();
            case TYPE_STR -> StringType.get();
            case TYPE_REF -> ReferenceType.get(type());
            default -> throw new ParseException("Invalid type -- " + prev.getType() + " " + prev.getSequence());
        };
    }

    private Value value() throws ParseException {
        Token previousToken = this.lookahead;
        next();
        return switch (previousToken.getType()) {
            case CONST_BOOLEAN -> new BoolValue(Objects.equals(previousToken.getSequence(), "true"));
            case CONST_INTEGER -> new IntValue(Integer.parseInt(previousToken.getSequence()));
            case CONST_STRING -> new StringValue(previousToken.getSequence());
            default -> throw new ParseException("Invalid constant");
        };
    }

    private Operand relationalOperand() throws ParseException {
        Token previousToken = this.lookahead;
        next();
        return switch (previousToken.getSequence()) {
            case "==" -> Operand.EQUAL;
            case "!=" -> Operand.NOT_EQUAL;
            case "<" -> Operand.LOWER;
            case ">" -> Operand.GREATER;
            case "<=" -> Operand.LOWER_OR_EQUAL;
            case ">=" -> Operand.GREATER_OR_EQUAL;
            default ->
                    throw new ParseException("Invalid operand for relexp -- %s".formatted(previousToken.getSequence()));
        };
    }

    private Operand arithmeticOperand() throws ParseException {
        Token previousToken = this.lookahead;
        next();
        return switch (previousToken.getSequence()) {
            case "+" -> Operand.ADD;
            case "-" -> Operand.SUB;
            case "*" -> Operand.MUL;
            case "/" -> Operand.DIV;
            case "^" -> Operand.EXP;
            default ->
                    throw new ParseException("Invalid operand for arithexp -- %s".formatted(previousToken.getSequence()));
        };
    }

    private Operand logicalOperand() throws ParseException {
        Token previousToken = this.lookahead;
        next();
        return switch (previousToken.getSequence()) {
            case "&" -> Operand.AND;
            case "|" -> Operand.OR;
            default -> throw new ParseException("Invalid operand for logexp");
        };
    }
}
