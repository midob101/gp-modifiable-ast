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
     * @return Token
     */
    public String getValue() {
        return token.getValue();
    }

    @Override
    public AbstractSyntaxTreeNode clone() {
        return new TokenTreeNode(this.token);
    }

    /**
     * Gets the display value for this tree node.
     *
     * @return String
     */
    public String getDisplayValue() {
        String displayValue = "TokenTreeNode: " + getToken().getLexerDefinition().getName();
        if(this.getAliases() != null) {
            displayValue += ", alias:" + this.getAliases();
        }
        displayValue += " | value: " + this.token.getValue();
        return displayValue;
    }

    protected String getSources() {
        return token.getValue();
    }
}
