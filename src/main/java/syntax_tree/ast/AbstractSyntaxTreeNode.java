package syntax_tree.ast;

import selectors.BaseSelector;
import syntax_tree.IPrintableTreeNode;
import syntax_tree.ast.exceptions.AddingConnectedNode;
import syntax_tree.ast.exceptions.ReplacingNonChildNode;
import syntax_tree.ast.exceptions.ReplacingUnconnectedNode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The abstract syntax tree node class.
 *
 * There are three
 */
public abstract class AbstractSyntaxTreeNode implements IPrintableTreeNode<AbstractSyntaxTreeNode> {
    private final LinkedList<AbstractSyntaxTreeNode> children = new LinkedList<>();
    private boolean hidden = false;
    private Set<String> alias = new HashSet<>();
    private AbstractSyntaxTreeNode parent = null;

    /**
     * @return Alias
     */
    public Set<String> getAliases() {
        return alias;
    }

    /**
     * Add a child to the end of this node.
     */
    public void addChild(AbstractSyntaxTreeNode child) throws AddingConnectedNode {
        if(child.getParent() == null) {
            children.add(child);
            child.setParent(this);
        } else {
            throw new AddingConnectedNode();
        }
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
    private void setParent(AbstractSyntaxTreeNode parent) {
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
    public boolean isVisible() {
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
        return (List<AbstractSyntaxTreeNode>) children.clone();
    }

    /**
     * Adds an alias to this node.
     *
     * @param value
     */
    public void addAlias(String value) {
        this.alias.add(value);
    }

    /**
     * Adds multiple alias to this node.
     *
     * @param aliases
     */
    public void addAliases(Set<String> aliases) {
        this.alias.addAll(aliases);
    }

    /**
     * Replaces one child in this node.
     *
     * @param original to be replaced
     * @param newChild the node to be added at the index of the original node
     */
    public void replaceChild(AbstractSyntaxTreeNode original, AbstractSyntaxTreeNode newChild) throws ReplacingNonChildNode, AddingConnectedNode {
        this.replaceChild(original, List.of(newChild));
    }

    /**
     * Replaces one child in this node.
     *
     * @param original to be replaced
     * @param newChildren the nodes to be added at the index of the original node
     */
    public void replaceChild(AbstractSyntaxTreeNode original, List<AbstractSyntaxTreeNode> newChildren) throws ReplacingNonChildNode, AddingConnectedNode {
        this.replaceChild(original, newChildren, false);
    }

    /**
     * Replaces one child in this node.
     *
     * @param original to be replaced
     * @param child the node to be added at the index of the original node
     * @param forced If true, the replacement is done even if a child already has a parent. This should be used with caution
     *               and only be used when entirely sure that the subtree is dangling. Mostly this will be the case in the
     *               AST generation and postprocessing rules.
     */
    public void replaceChild(AbstractSyntaxTreeNode original, AbstractSyntaxTreeNode child, boolean forced) throws ReplacingNonChildNode, AddingConnectedNode {
        this.replaceChild(original, List.of(child), forced);
    }

    /**
     * Replaces one child in this node.
     *
     * @param original to be replaced
     * @param newChildren the nodes to be added at the index of the original node
     * @param forced If true, the replacement is done even if a child already has a parent. This should be used with caution
     *               and only be used when entirely sure that the subtree is dangling. Mostly this will be the case in the
     *               AST generation and postprocessing rules.
     */
    public void replaceChild(AbstractSyntaxTreeNode original, List<AbstractSyntaxTreeNode> newChildren, boolean forced) throws ReplacingNonChildNode, AddingConnectedNode {
        if(original.parent == this) {
            if(!forced) {
                if(newChildren.stream().anyMatch((child) -> child.getParent() != null)) {
                    throw new AddingConnectedNode();
                }
            }
            int idx = children.indexOf(original);
            children.addAll(idx, newChildren);
            children.remove(original);
            original.setParent(null);
            for(AbstractSyntaxTreeNode child: newChildren) {
                child.setParent(this);
            }
        } else {
            throw new ReplacingNonChildNode();
        }
    }

    /**
     * Replaces this node with a different tree node
     */
    public void replace(AbstractSyntaxTreeNode replaceWith) throws ReplacingUnconnectedNode {
        this.replace(List.of(replaceWith));
    }

    /**
     * Replaces this node with different tree nodes
     */
    public void replace(List<AbstractSyntaxTreeNode> replaceWith) throws ReplacingUnconnectedNode {
        if(this.parent != null) {
            try {
                this.parent.replaceChild(this, replaceWith);
            } catch (ReplacingNonChildNode|AddingConnectedNode e) {
                // We can safely ignore the exception at this point. We know, that the child is a direct
                // child of the node.
                throw new RuntimeException(e);
            }
        } else {
            throw new ReplacingUnconnectedNode();
        }
    }

    /**
     * Removes this node
     */
    public void remove() {
        if(this.parent != null) {
            this.parent.removeChild(this);
        }
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
     * Gets the display value of this node, used for the AST display.
     */
    public abstract String getDisplayValue();

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

    public AbstractSyntaxTreeNode deepClone() {
        AbstractSyntaxTreeNode newInstance;
        try {
            newInstance = this.clone();
            newInstance.copyAttributes(this);
            for(AbstractSyntaxTreeNode child: children) {
                newInstance.addChild(child.deepClone());
            }
        } catch (AddingConnectedNode e) {
            throw new RuntimeException(e);
        }
        return newInstance;
    }

    public abstract AbstractSyntaxTreeNode clone();

    private void copyAttributes(AbstractSyntaxTreeNode abstractSyntaxTreeNode) {
        this.alias = abstractSyntaxTreeNode.getAliases();
        this.hidden = abstractSyntaxTreeNode.hidden;
    }

    /**
     * Gets the source code directly correlated to this node in particular. Does not include the children source code.
     */
    protected abstract String getSources();

    /**
     * Replaces one child in this node.
     */
    private void removeChild(AbstractSyntaxTreeNode child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * Internal recursive function to create the source code.
     */
    private void createSourceCode(StringBuilder builder) {
        builder.append(this.getSources());
        for(AbstractSyntaxTreeNode treeNode: getAllChildren()) {
            treeNode.createSourceCode(builder);
        }
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
