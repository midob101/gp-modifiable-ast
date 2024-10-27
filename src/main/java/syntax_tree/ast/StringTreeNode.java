package syntax_tree.ast;

public class StringTreeNode extends AbstractSyntaxTreeNode {
    private final String value;

    public StringTreeNode(String value) {
        this.value = value;
    }
    /**
     * Gets the display value for this tree node.
     *
     * @return String
     */
    public String getDisplayValue() {
        return this.value;
    }

    protected String getSources() {
        return this.value;
    }
}
