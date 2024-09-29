package lexer;

import java.util.Objects;

public final class Token {
    private final LexerDefinition lexerDefinition;
    private final String value;
    private boolean keepInAST;

    public Token(LexerDefinition lexerDefinition, String value) {
        this.lexerDefinition = lexerDefinition;
        this.value = value;
        this.keepInAST = lexerDefinition.getKeepInAST();
    }
    public Token(LexerDefinition lexerDefinition, String value, boolean keepInAST) {
        this.lexerDefinition = lexerDefinition;
        this.value = value;
        this.keepInAST = keepInAST;
    }

    public LexerDefinition getLexerDefinition() {
        return lexerDefinition;
    }

    public String getValue() {
        return value;
    }

    public void setKeepInAST(boolean keepInAST) {
        this.keepInAST = keepInAST;
    }

    public boolean getKeepInAST() {
        return keepInAST;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Token) obj;
        return Objects.equals(this.lexerDefinition, that.lexerDefinition) &&
                Objects.equals(this.value, that.value) &&
                this.keepInAST == that.keepInAST;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexerDefinition, value, keepInAST);
    }

    @Override
    public String toString() {
        return "Token[" +
                "lexerDefinition=" + lexerDefinition + ", " +
                "value=" + value + ", " +
                "keepInAST=" + keepInAST + ']';
    }

}
