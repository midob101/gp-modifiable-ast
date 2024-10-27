package syntax_tree.ast;

import lexer.Token;

public class TokenTreeNode extends AbstractSyntaxTreeNode {

    private final Token token;

    public TokenTreeNode(Token token) {
        this.token = token;
    }

    /**
     * @return Token
     */
    public Token getToken() {
        return token;
    }
    /**
     * Gets the display value for this tree node.
     *
     * @return String
     */
    public String getDisplayValue() {
        return (this.getAlias() != null ? this.getAlias() : getToken().getLexerDefinition().getName()) + ": " + getToken().getValue();
    }

    protected String getSources() {
        return token.getValue();
    }
}
