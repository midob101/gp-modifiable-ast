package selectors.data;

import syntax_tree.AbstractSyntaxTreeNode;
import selectors.BaseSelector;

/**
 * Selector that matches nodes with a specific alias.
 */
public class AliasSelector extends BaseSelector {
    private final String alias;

    public AliasSelector(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return treeNode.getAlias() != null && treeNode.getAlias().equals(this.alias);
    }
}
