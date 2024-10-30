package selectors.data;

import selectors.BaseSelector;
import syntax_tree.ast.AbstractSyntaxTreeNode;
import syntax_tree.ast.TokenTreeNode;

/**
 * Selector that matches nodes with a specific token name.
 */
public class TokenValueSelector extends BaseSelector {
    private final String tokenValue;

    public TokenValueSelector(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        if(treeNode instanceof TokenTreeNode convertedNode) {
            return convertedNode.getToken().getValue().equals(this.tokenValue);
        }
        return false;
    }
}
