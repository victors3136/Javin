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
import interpreter.model.type.Type;
import interpreter.model.values.BoolValue;
import interpreter.model.values.IntValue;
import interpreter.model.values.StringValue;

import java.util.Objects;

public class RecursiveDescentParser implements Parser {
    TokenStack tokens;
    Token lookahead;

    private void moveToNext() {
        if (tokens.isEmpty())
            lookahead = null;
        else lookahead = tokens.pop();
    }

    public Statement buildProgram(TokenStack tokens) throws ParseException, ExpressionException {
        this.tokens = tokens;
        this.lookahead = this.tokens.pop();
        return buildStatement();
    }

    private Statement buildStatement() throws ParseException, ExpressionException {
        if (this.lookahead == null)
            return new NoOperationStatement();
        switch (this.lookahead.getType()) {
            case KEYWORD_COMPOUND -> {
                moveToNext();
                return new CompoundStatement(
                        buildStatement(),
                        buildStatement()
                );
            }
            case KEYWORD_IF -> {
                moveToNext();
                Expression condition = buildExpression();
                if (this.lookahead.getType() == TokenType.KEYWORD_BRANCH) {
                    moveToNext();
                    return new IfStatement(condition, buildStatement(), buildStatement());
                } else
                    return new IfStatement(condition, buildStatement(), new NoOperationStatement());
            }
            case KEYWORD_PRINT -> {
                moveToNext();
                return new PrintStatement(buildExpression());
            }
            case EMPTY -> {
                moveToNext();
                return new NoOperationStatement();
            }
            case ASSIGNMENT_OP -> {
                moveToNext();
                return new AssignStatement(getIdentifier(), buildExpression());
            }
            case TYPE_BOOL, TYPE_INT, TYPE_STR -> {
                Token previousToken = this.lookahead;
                moveToNext();
                return new VariableDeclarationStatement(switch (previousToken.getType()) {
                    case TYPE_BOOL -> new Type(BoolValue.class);
                    case TYPE_INT -> new Type(IntValue.class);
                    case TYPE_STR -> new Type(StringValue.class);
                    default -> null;
                },
                        getIdentifier());
            }
            case KEYWORD_OPEN_FILE -> {
                moveToNext();
                return new OpenReadFileStatement(buildExpression());
            }
            case KEYWORD_READ_FILE -> {
                moveToNext();
                return new ReadFileStatement(buildExpression(), getIdentifier());
            }
            case KEYWORD_CLOSE_FILE -> {
                moveToNext();
                return new CloseFileStatement(buildExpression());
            }
            default -> throw new ParseException("Invalid token for Statement -- " + lookahead.getSequence());
        }
    }

    private String getIdentifier() {
        String id = this.lookahead.getSequence();
        moveToNext();
        return id;
    }

    private Expression buildExpression() throws ParseException, ExpressionException {
        switch (this.lookahead.getType()) {
            case EXP_OP, MUL_DIV_OP, ADD_SUB_OP -> {
                Token previousToken = this.lookahead;
                moveToNext();
                return new ArithmeticExpression(
                        switch (previousToken.getSequence()) {
                            case "^" -> Operand.EXP;
                            case "*" -> Operand.MUL;
                            case "/" -> Operand.DIV;
                            case "+" -> Operand.ADD;
                            case "-" -> Operand.SUB;
                            default -> throw new ParseException("Invalid operand for arithexp");
                        },
                        buildExpression(),
                        buildExpression()
                );
            }
            case LOGICAL_OP -> {
                Token preiviousToken = this.lookahead;
                moveToNext();
                return new LogicExpression(
                        switch (preiviousToken.getSequence()) {
                            case "&" -> Operand.AND;
                            case "|" -> Operand.OR;
                            default -> throw new ParseException("Invalid operand for logexp");
                        },
                        buildExpression(),
                        buildExpression()
                );
            }
            case RELATIONAL_OP -> {
                Token previousToken = this.lookahead;
                moveToNext();
                return new RelationalExpression(
                        switch (previousToken.getSequence()) {
                            case "==" -> Operand.EQUAL;
                            case "!=" -> Operand.NOT_EQUAL;
                            case "<" -> Operand.LOWER;
                            case ">" -> Operand.GREATER;
                            case "<=" -> Operand.LOWER_OR_EQUAL;
                            case ">=" -> Operand.GREATER_OR_EQUAL;
                            default -> throw new ParseException("Invalid operand for relexp");
                        },
                        buildExpression(),
                        buildExpression()
                );
            }
            case CONST_BOOLEAN, CONST_INTEGER, CONST_STRING -> {
                Token previousToken = this.lookahead;
                moveToNext();
                return new ValueExpression(
                        switch (previousToken.getType()) {
                            case CONST_BOOLEAN -> new BoolValue(Objects.equals(previousToken.getSequence(), "true"));
                            case CONST_INTEGER -> new IntValue(Integer.parseInt(previousToken.getSequence()));
                            case CONST_STRING -> new StringValue(previousToken.getSequence());
                            default -> throw new ParseException("Invalid constant");
                        }
                );
            }
            case IDENTIFIER -> {
                Token previousToken = this.lookahead;
                moveToNext();
                return new VariableExpression(previousToken.getSequence());
            }
            default -> throw new ParseException("Invalid token for Expression -- " + lookahead.getSequence());
        }
    }
}
