package selectors.data;

import selectors.BaseSelector;
import syntax_tree.ast.AbstractSyntaxTreeNode;
import syntax_tree.ast.TokenTreeNode;

/**
 * Selector that matches nodes with a specific token name.
 */
public class TokenSelector extends BaseSelector {
    private final String tokenName;

    public TokenSelector(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        if(treeNode instanceof TokenTreeNode convertedNode) {
            return convertedNode.getToken().getLexerDefinition().getName().equals(this.tokenName);
        }
        return false;
    }
}
