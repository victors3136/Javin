package inputmanager.tokenizer;

public record Token(TokenType type, String sequence) {
    @Override
    public String toString() {
        return type() + " " + sequence();
    }

    public Token(TokenType type, String sequence) {
        this.type = type;
        if (type != TokenType.TYPE_STR) {
            this.sequence = sequence;
            return;
        }
        this.sequence = sequence.replaceAll("^\"|\"$", "");
    }
}
