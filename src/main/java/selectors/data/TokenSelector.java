package selectors.data;

import selectors.BaseSelector;
import syntax_tree.AbstractSyntaxTreeNode;

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
        return treeNode.getToken() != null && treeNode.getToken().getLexerDefinition().getName().equals(this.tokenName);
    }
}
