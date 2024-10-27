package selectors.logical;

import selectors.BaseSelector;
import syntax_tree.AbstractSyntaxTreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * Selector that can combine multiple other selectors.
 * Matches only on notes where all other selectors match.
 */
public class AndSelector extends BaseSelector {
    private final List<BaseSelector> selectors;

    public AndSelector(List<BaseSelector> selectors) {
        this.selectors = selectors;
    }

    public AndSelector(BaseSelector... selectors) {
        this.selectors = Arrays.stream(selectors).toList();
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return this.selectors.stream().allMatch((selector) -> selector.matches(treeNode));
    }
}
