package inputmanager.tokenizer;

import inputmanager.tokenstack.TokenStack;

public interface Tokenizer {
    TokenStack tokenize(String source) throws TokenizerException;
}
