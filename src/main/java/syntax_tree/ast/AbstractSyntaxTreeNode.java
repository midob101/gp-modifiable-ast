package syntax_tree.ast;

import grammar.GrammarRule;
import lexer.Token;
import selectors.BaseSelector;
import syntax_tree.IPrintableTreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * The abstract syntax tree node class.
 *
 * There are three
 */
public abstract class AbstractSyntaxTreeNode implements IPrintableTreeNode<AbstractSyntaxTreeNode> {
    private final LinkedList<AbstractSyntaxTreeNode> children = new LinkedList<>();
    private boolean hidden = false;
    private String alias = null;
    private AbstractSyntaxTreeNode parent = null;


    /**
     * @return Alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Add a child to the end of this node.
     *
     * @param child
     */
    public void addChild(AbstractSyntaxTreeNode child) {
        children.add(child);
    }

    /**
     * Sets the hidden flag.
     */
    public void setHidden() {
        this.hidden = true;
    }

    /**
     * Sets the parent node
     *
     * @param parent
     */
    public void setParent(AbstractSyntaxTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Gets the parent node
     *
     * @return parent
     */
    public AbstractSyntaxTreeNode getParent() {
        return parent;
    }

    /**
     * Checks if this node is visible.
     *
     * @return boolean
     */
    private boolean isVisible() {
        return !this.hidden;
    }

    /**
     * Gets the visible, immediate children.
     *
     * @return the filtered list of children
     */
    @Override
    public List<AbstractSyntaxTreeNode> getChildren() {
        return children.stream().filter(AbstractSyntaxTreeNode::isVisible).toList();
    }

    /**
     * Gets all immediate children, including the hidden ones of this node.
     *
     * @return the complete list of children
     */
    public List<AbstractSyntaxTreeNode> getAllChildren() {
        return children;
    }

    /**
     * Sets the alias of this node.
     *
     * @param value
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Replaces one child in this node.
     *
     * @param original to be replaced
     * @param newChilds the nodes to be added at the index of the original node
     */
    public void replaceChild(AbstractSyntaxTreeNode original, List<AbstractSyntaxTreeNode> newChilds) {
        int idx = children.indexOf(original);
        children.addAll(idx, newChilds);
        children.remove(original);
    }

    /**
     * Replaces this node with a string
     * @param replaceWith
     */
    public void replaceSelf(AbstractSyntaxTreeNode replaceWith) {
        this.parent.replaceChild(this, List.of(replaceWith));
    }

    /**
     * Gets the source code of this ast.
     *
     * @return String
     */
    public String getSourceCode() {
        StringBuilder sb = new StringBuilder();
        createSourceCode(sb);
        return sb.toString();
    }

    protected abstract String getSources();
    public abstract String getDisplayValue();

    /**
     * Internal recursive function to create the source code.
     *
     * @param builder
     */
    private void createSourceCode(StringBuilder builder) {
        builder.append(this.getSources());
        for(AbstractSyntaxTreeNode treeNode: getAllChildren()) {
            treeNode.createSourceCode(builder);
        }
    }

    /**
     * Finds all nodes in the entire subtree matching this selector.
     * The result may include the root node on which it was called.
     *
     * Performs a preordering depth first search on the root node.
     *
     * @param selector The selector to search for.
     * @return The matches
     */
    public QueryResult query(BaseSelector selector) {
        List<AbstractSyntaxTreeNode> matches = new LinkedList<>();
        this.queryRecursive(selector, matches);
        return new QueryResult(matches);
    }

    /**
     * Finds all nodes in the entire subtree matching this selector.
     * The result can not include the root node on which it was called.
     *
     * Performs a preordering depth first search on the root node.
     *
     * @param selector The selector to search for.
     * @return The matches
     */
    public QueryResult queryChildren(BaseSelector selector) {
        List<AbstractSyntaxTreeNode> matches = new LinkedList<>();
        for(AbstractSyntaxTreeNode child: this.getAllChildren()) {
            child.queryRecursive(selector, matches);
        }
        return new QueryResult(matches);
    }


    /**
     * Finds all nodes in the immediate children matching this selector.
     * The result can not include the root node on which it was called.
     *
     * @param selector The selector to search for.
     * @return The matches
     */
    public QueryResult queryImmediateChildren(BaseSelector selector) {
        List<AbstractSyntaxTreeNode> matches = new LinkedList<>();

        for(AbstractSyntaxTreeNode child: this.getAllChildren()) {
            if(selector.matches(child)) {
                matches.add(child);
            }
        }

        return new QueryResult(matches);
    }

    /**
     * Internal recursive function to query an entire subtree.
     *
     * @param selector the selector to search for
     * @param matches the list of matches
     */
    private void queryRecursive(BaseSelector selector, List<AbstractSyntaxTreeNode> matches) {
        if(selector.matches(this)) {
            matches.add(this);
        }

        for(AbstractSyntaxTreeNode child: this.getAllChildren()) {
            child.queryRecursive(selector, matches);
        }
    }
}
