package test_selectors;

import selectors.BaseSelector;
import syntax_tree.ast.AbstractSyntaxTreeNode;

public class FalseSelector extends BaseSelector {
    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return false;
    }
}
