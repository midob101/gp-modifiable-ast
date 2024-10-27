package selectors.logical;

import syntax_tree.AbstractSyntaxTreeNode;
import selectors.BaseSelector;

import java.util.Arrays;
import java.util.List;

/**
 * Selector that can combine multiple other selectors.
 * Matches on notes where one or more of the other selectors match.
 */
public class OrSelector extends BaseSelector {
    private final List<BaseSelector> selectors;

    public OrSelector(List<BaseSelector> selectors) {
        this.selectors = selectors;
    }

    public OrSelector(BaseSelector... selectors) {
        this.selectors = Arrays.stream(selectors).toList();
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return this.selectors.stream().anyMatch((selector) -> selector.matches(treeNode));
    }
}
