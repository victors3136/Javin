package inputmanager.tokenizer;

public class Token {
    final TokenType type;
    final String sequence;
    public String getSequence(){ return sequence; }
    public TokenType getType() { return type; }
    @Override
    public String toString(){
        return getType()+" "+getSequence();
    }
    public Token (TokenType type, String sequence){
        this.type = type;
        if(type != TokenType.TYPE_STR) {
            this.sequence = sequence;
            return;
        }
        this.sequence = sequence.replaceAll("^\"|\"$", "");
    }
    public Token(Token t) {
        this.type = t.type;
        this.sequence = t.sequence;
    }
}
