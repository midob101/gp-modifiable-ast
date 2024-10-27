package syntax_tree;

import grammar.GrammarRule;
import lexer.Token;
import selectors.BaseSelector;

import java.util.LinkedList;
import java.util.List;

/**
 * The abstract syntax tree node class.
 *
 * There are three
 */
public class AbstractSyntaxTreeNode implements IPrintableTreeNode<AbstractSyntaxTreeNode> {
    private String value = "";
    private Token token = null;
    private GrammarRule rule = null;
    private final LinkedList<AbstractSyntaxTreeNode> children = new LinkedList<>();
    private boolean hidden = false;
    private String alias = null;
    private AbstractSyntaxTreeNode parent = null;

    /**
     * Creates a tree node based on a lexer token
     */
    public AbstractSyntaxTreeNode(Token token) {
        this.token = token;
    }

    /**
     * Creates a tree node based on a grammar production rule
     */
    public AbstractSyntaxTreeNode(GrammarRule rule) {
        this.rule = rule;
    }


    /**
     * Creates a tree node based on a string
     */
    public AbstractSyntaxTreeNode(String value) {
        this.value = value;
    }

    /**
     * @return Token
     */
    public Token getToken() {
        return token;
    }

    /**
     * @return Alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @return production
     */
    public GrammarRule getRule() {
        return rule;
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
    protected void setHidden() {
        this.hidden = true;
    }

    /**
     * Sets the parent node
     *
     * @param parent
     */
    protected void setParent(AbstractSyntaxTreeNode parent) {
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
     * Gets the display value for this tree node.
     *
     * @return String
     */
    @Override
    public String getDisplayValue() {
        if(this.rule != null) {
            return this.alias != null ? this.alias : getRule().leftHandSymbol().name();
        } else if(getToken() != null) {
            return (this.alias != null ? this.alias : getToken().getLexerDefinition().getName()) + ": " + getToken().getValue();
        } else if(!this.value.isBlank()) {
            return this.value;
        }
        return "UNDEFINED";
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
    public void replaceSelfWithString(String replaceWith) {
        this.parent.replaceChild(this, List.of(new AbstractSyntaxTreeNode(replaceWith)));
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

    /**
     * Internal recursive function to create the source code.
     *
     * @param builder
     */
    private void createSourceCode(StringBuilder builder) {
        if(this.token != null) {
            builder.append(token.getValue());
        }
        if(this.value != null) {
            builder.append(this.value);
        }
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
