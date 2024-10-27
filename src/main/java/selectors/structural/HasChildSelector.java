package selectors.structural;

import selectors.BaseSelector;
import syntax_tree.AbstractSyntaxTreeNode;

/**
 * Selector that matches on nodes that have atleast one child that matches another selector.
 */
public class HasChildSelector extends BaseSelector {

    private final BaseSelector selector;

    public HasChildSelector(BaseSelector selector) {
        this.selector = selector;
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return !treeNode.queryChildren(selector).isEmpty();
    }
}
