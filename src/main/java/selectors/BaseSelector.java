package selectors;

import syntax_tree.AbstractSyntaxTreeNode;

/**
 * Base class for a selector.
 *
 * A selector can be defined to check if an AST node matches a specific property.
 */
public abstract class BaseSelector {

    public abstract boolean matches(AbstractSyntaxTreeNode treeNode);
}
