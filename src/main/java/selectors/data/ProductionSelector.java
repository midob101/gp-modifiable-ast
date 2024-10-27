package selectors.data;

import syntax_tree.AbstractSyntaxTreeNode;
import selectors.BaseSelector;

/**
 * Selector that matches nodes with a specific production name.
 */
public class ProductionSelector extends BaseSelector {
    private final String production;

    public ProductionSelector(String production) {
        this.production = production;
    }

    @Override
    public boolean matches(AbstractSyntaxTreeNode treeNode) {
        return treeNode.getRule() != null && treeNode.getRule().leftHandSymbol().name().equals(this.production);
    }
}
