package selectors.data;

import syntax_tree.ast.AbstractSyntaxTreeNode;
import selectors.BaseSelector;
import syntax_tree.ast.ProductionTreeNode;

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
        if(treeNode instanceof ProductionTreeNode convertedNode) {
            return convertedNode.getProduction().leftHandSymbol().name().equals(this.production);
        }
        return false;
    }
}
