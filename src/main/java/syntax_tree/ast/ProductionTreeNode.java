package syntax_tree.ast;

import grammar.Production;

public class ProductionTreeNode extends AbstractSyntaxTreeNode {

    private final Production production;

    /**
     * Creates a tree node based on a grammar production
     */
    public ProductionTreeNode(Production production) {
        this.production = production;
    }

    /**
     * @return production
     */
    public Production getProduction() {
        return production;
    }

    /**
     * Gets the display value for this tree node.
     *
     * @return String
     */
    @Override
    public String getDisplayValue() {
        String displayValue = "ProductionTreeNode: " + getProduction().leftHandSymbol().name();
        if(this.getAliases() != null) {
            displayValue += ", aliases:" + this.getAliases().toString();
        }
        return displayValue;
    }

    @Override
    protected String getSources() {
        return "";
    }
}
