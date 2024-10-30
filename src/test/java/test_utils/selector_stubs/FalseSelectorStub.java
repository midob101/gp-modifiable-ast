package test_utils.selector_stubs;

import selectors.BaseSelector;
import syntax_tree.ast.AbstractSyntaxTreeNode;

public class FalseSelectorStub extends BaseSelector {
    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return false;
    }
}
