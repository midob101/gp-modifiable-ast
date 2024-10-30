package selectors.structural;

import selectors.BaseSelector;
import syntax_tree.ast.AbstractSyntaxTreeNode;

/**
 * Selector that matches on nodes that have atleast one child that matches another selector.
 */
public class HasImmediateChildSelector extends BaseSelector {

    private final BaseSelector selector;

    public HasImmediateChildSelector(BaseSelector selector) {
        this.selector = selector;
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return !treeNode.queryImmediateChildren(selector).isEmpty();
    }
}
