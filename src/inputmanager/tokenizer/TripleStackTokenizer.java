package inputmanager.tokenizer;

import inputmanager.tokenstack.TokenStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TripleStackTokenizer implements Tokenizer {
    Set<TokenType> tokenTypes;

    private final Map<TokenType, Pattern> precompiledTokenPatterns;

    public TripleStackTokenizer() {
        tokenTypes = EnumSet.allOf(TokenType.class);
        precompiledTokenPatterns = precompileRegularExps(tokenTypes);
    }

    public TokenStack tokenize(String source) throws TokenizerException {
        return transformSequenceToPrefix(createTokenSequence(source));
    }

    private Map<TokenType, Pattern> precompileRegularExps(Set<TokenType> tokenTypes) {
        Map<TokenType, Pattern> map = new HashMap<>();
        for (TokenType token : tokenTypes) {
            map.put(token, Pattern.compile(token.toRegex()));
        }
        return map;
    }

    private TokenStack createTokenSequence(String src) throws TokenizerException {
        TokenStack ts = new TokenStack();
        String source = src.strip();
        ts.clear();
        while (!source.isEmpty()) {
            boolean match = false;
            for (TokenType tok : tokenTypes) {
                Matcher result = precompiledTokenPatterns.get(tok).matcher(source);
                if (result.find()) {
                    match = true;
                    String actualTokenString = result.group().trim();
                    if (!ts.isEmpty() && ((ts.top().getType() == TokenType.OPEN_PARENTHESIS
                            && tok == TokenType.CLOSED_PARENTHESIS) || (ts.top().getType() == TokenType.KEYWORD_COMPOUND
                            && tok == TokenType.KEYWORD_COMPOUND)))
                        ts.push(new Token(TokenType.EMPTY, "NOTHING HERE"));
                    ts.push(new Token(tok, actualTokenString));
                    source = result.replaceFirst("").strip();
                    break;
                }
            }
            if (!match) {
                throw new TokenizerException("Unknown symbol in string : " + source);
            }
        }
        if (ts.top().getType() == TokenType.KEYWORD_COMPOUND)
            ts.push(new Token(TokenType.EMPTY, "NOTHING HERE"));
        return ts;
    }

    private TokenStack transformSequenceToPrefix(TokenStack inputStack) throws TokenizerException {
        TokenStack auxStack = new TokenStack();
        TokenStack resultStack = new TokenStack();
        while (!inputStack.isEmpty()) {
            Token current = inputStack.pop();
            switch (current.getType()) {
                case IDENTIFIER,
                        CONST_BOOLEAN,
                        CONST_INTEGER,
                        CONST_STRING,
                        TYPE_BOOL,
                        TYPE_INT,
                        TYPE_STR,
                        EMPTY -> resultStack.push(current);
                case CLOSED_PARENTHESIS,
                        KEYWORD_PRINT,
                        KEYWORD_BRANCH,
                        KEYWORD_IF,
                        KEYWORD_CLOSE_FILE,
                        KEYWORD_READ_FILE,
                        KEYWORD_OPEN_FILE -> auxStack.push(current);

                case KEYWORD_COMPOUND -> {
                    while ((!auxStack.isEmpty())
                            && (auxStack.top().getType() != TokenType.CLOSED_PARENTHESIS)
                            && (auxStack.top().getType() != TokenType.KEYWORD_COMPOUND)) {
                        resultStack.push(auxStack.pop());
                    }
                    if (!auxStack.isEmpty() && auxStack.top().getType() == TokenType.KEYWORD_COMPOUND)
                        resultStack.push(auxStack.pop());
                    auxStack.push(current);
                }
                case OPEN_PARENTHESIS -> {
                    while (!auxStack.isEmpty() && auxStack.top().getType() != TokenType.CLOSED_PARENTHESIS)
                        resultStack.push(auxStack.pop());
                    if (auxStack.isEmpty())
                        throw new TokenizerException("Unbalanced parentheses -- extra open p.");
                    auxStack.pop();
                }
                case EXP_OP,
                        MUL_DIV_OP,
                        ADD_SUB_OP,
                        ASSIGNMENT_OP,
                        RELATIONAL_OP,
                        LOGICAL_OP -> {
                    while (!auxStack.isEmpty() && auxStack.top().getType().greaterOrEqualPrecedence(current.getType()))
                        resultStack.push(auxStack.pop());
                    auxStack.push(current);
                }
            }
        }
        while (!auxStack.isEmpty()) {
            if (auxStack.top().getType() == TokenType.CLOSED_PARENTHESIS)
                throw new TokenizerException("Unbalanced parentheses -- extra closed p.");
            else resultStack.push(auxStack.pop());
        }
        return resultStack;
    }
}
