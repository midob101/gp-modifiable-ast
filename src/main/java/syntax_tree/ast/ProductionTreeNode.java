package syntax_tree.ast;

import grammar.GrammarRule;

public class ProductionTreeNode extends AbstractSyntaxTreeNode {

    private final GrammarRule production;

    /**
     * Creates a tree node based on a grammar production rule
     */
    public ProductionTreeNode(GrammarRule rule) {
        this.production = rule;
    }

    /**
     * @return production
     */
    public GrammarRule getRule() {
        return production;
    }

    /**
     * Gets the display value for this tree node.
     *
     * @return String
     */
    @Override
    public String getDisplayValue() {
        return this.getAlias() != null ? this.getAlias() : getRule().leftHandSymbol().name();
    }

    @Override
    protected String getSources() {
        return "";
    }
}
