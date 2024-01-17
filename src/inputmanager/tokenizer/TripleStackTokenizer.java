package inputmanager.tokenizer;

import inputmanager.tokenstack.TokenStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TripleStackTokenizer implements Tokenizer {
    static final Set<TokenType> tokenTypes = EnumSet.allOf(TokenType.class);
    private static final Map<TokenType, Pattern> precompiledTokenPatterns = precompileRegularExps();

    public TripleStackTokenizer() {
    }

    public TokenStack tokenize(String source) throws TokenizerException {
        return transformSequenceToPrefix(createTokenSequence(source));
    }

    static private Map<TokenType, Pattern> precompileRegularExps() {
        Map<TokenType, Pattern> map = new HashMap<>();
        for (TokenType token : TripleStackTokenizer.tokenTypes) {
            map.put(token, Pattern.compile(token.regex()));
        }
        return map;
    }

    private static boolean tokenizationConditionForEmptyStatement(TokenStack tokens, TokenType current) {
        return !tokens.isEmpty() && ((
                                             tokens.top().type() == TokenType.OPEN_PARENTHESIS
                                             &&
                                             current == TokenType.CLOSED_PARENTHESIS
                                     ) || (
                                             tokens.top().type() == TokenType.KEYWORD_COMPOUND
                                             &&
                                             current == TokenType.KEYWORD_COMPOUND
                                     ));
    }

    private TokenStack createTokenSequence(String src) throws TokenizerException {
        TokenStack tokenStack = new TokenStack();
        StringBuilder source = new StringBuilder(src.strip());
        tokenStack.clear();
        if (src.isEmpty()) {
            tokenStack.push(new Token(TokenType.EMPTY_STATEMENT, source.toString()));
            return tokenStack;
        }
        while (!source.isEmpty()) {
            boolean match = false;
            for (TokenType token : tokenTypes) {
                Matcher result = precompiledTokenPatterns.get(token).matcher(source);
                if (result.find()) {
                    match = true;
                    String string = result.group().trim();
                    if (tokenizationConditionForEmptyStatement(tokenStack, token))
                        tokenStack.push(new Token(TokenType.EMPTY_STATEMENT, "NOTHING HERE"));
                    tokenStack.push(new Token(token, string));
                    source = new StringBuilder(result.replaceFirst("").strip());
                    break;
                }
            }
            if (!match) {
                throw new TokenizerException("Unknown symbol in string : " + source);
            }
        }
        if (tokenStack.top().type() == TokenType.KEYWORD_COMPOUND)
            tokenStack.push(new Token(TokenType.EMPTY_STATEMENT, "NOTHING HERE"));
        return tokenStack;
    }

    private void transformSequenceToPrefixDefaultTokenToResult(Token current, TokenStack stack) {
        stack.push(current);
    }

    private void transformSequenceToPrefixCommaToken(TokenStack auxStack, TokenStack resultStack) {
        while ((!auxStack.isEmpty())
               && (auxStack.top().type() != TokenType.CLOSED_PARENTHESIS)
               && (auxStack.top().type() != TokenType.KEYWORD_COMPOUND)) {
            resultStack.push(auxStack.pop());
        }
    }

    private void transformSequenceToPrefixCompoundToken(Token current, TokenStack auxStack, TokenStack resultStack) {
        while ((!auxStack.isEmpty())
               && (auxStack.top().type() != TokenType.CLOSED_PARENTHESIS)
               && (auxStack.top().type() != TokenType.KEYWORD_COMPOUND)) {
            resultStack.push(auxStack.pop());
        }
        if (!auxStack.isEmpty() && auxStack.top().type() == TokenType.KEYWORD_COMPOUND)
            resultStack.push(auxStack.pop());
        auxStack.push(current);
    }

    private void transformSequenceToPrefixOpenParenToken(TokenStack auxStack, TokenStack resultStack) throws TokenizerException {
        while (!auxStack.isEmpty() && auxStack.top().type() != TokenType.CLOSED_PARENTHESIS)
            resultStack.push(auxStack.pop());
        if (auxStack.isEmpty())
            throw new TokenizerException("Unbalanced parentheses -- extra open p.");
        auxStack.pop();
    }

    private void transformSequenceToPrefixOperationToken(Token current, TokenStack auxStack, TokenStack resultStack) {
        while (!auxStack.isEmpty() && auxStack.top().type().compare(current.type()))
            resultStack.push(auxStack.pop());
        auxStack.push(current);
    }

    private TokenStack transformSequenceToPrefix(TokenStack inputStack) throws TokenizerException {
        TokenStack auxStack = new TokenStack();
        TokenStack resultStack = new TokenStack();
        while (!inputStack.isEmpty()) {
            Token current = inputStack.pop();
            switch (current.type()) {
                case COMMA -> transformSequenceToPrefixCommaToken(auxStack, resultStack);
                case IDENTIFIER,
                        CONST_BOOLEAN,
                        CONST_INTEGER,
                        CONST_STRING,
                        TYPE_BOOL,
                        TYPE_INT,
                        TYPE_STR,
                        TYPE_REF,
                        KEYWORD_HEAP_READ,
                        EMPTY_STATEMENT -> transformSequenceToPrefixDefaultTokenToResult(current, resultStack);
                case CLOSED_PARENTHESIS,
                        KEYWORD_PRINT,
                        KEYWORD_BRANCH,
                        KEYWORD_IF,
                        KEYWORD_FORK,
                        KEYWORD_WHILE,
                        KEYWORD_CLOSE_FILE,
                        KEYWORD_READ_FILE,
                        KEYWORD_OPEN_FILE,
                        KEYWORD_HEAP_ALLOC,
                        KEYWORD_HEAP_WRITE -> transformSequenceToPrefixDefaultTokenToResult(current, auxStack);
                case KEYWORD_COMPOUND -> transformSequenceToPrefixCompoundToken(current, auxStack, resultStack);
                case OPEN_PARENTHESIS -> transformSequenceToPrefixOpenParenToken(auxStack, resultStack);
                case EXP_OP,
                        MUL_DIV_OP,
                        ADD_SUB_OP,
                        ASSIGNMENT_OP,
                        RELATIONAL_OP,
                        LOGICAL_OP -> transformSequenceToPrefixOperationToken(current, auxStack, resultStack);
            }
        }
        while (!auxStack.isEmpty()) {
            if (auxStack.top().type() == TokenType.CLOSED_PARENTHESIS)
                throw new TokenizerException("Unbalanced parentheses -- extra closed p.");
            else resultStack.push(auxStack.pop());
        }
        return resultStack;
    }

}
