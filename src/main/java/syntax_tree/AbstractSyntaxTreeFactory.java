package syntax_tree;

import grammar.GrammarRule;
import grammar.Symbol;
import grammar.SymbolModifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractSyntaxTreeFactory {
    /**
     * Creates an AST from a CST.
     *
     * @param concreteSyntaxTreeNode the root node of the CST
     * @return the AST
     */
    public static AbstractSyntaxTreeNode create(ConcreteSyntaxTreeNode concreteSyntaxTreeNode) {
        AbstractSyntaxTreeNode root = createRecursive(concreteSyntaxTreeNode);
        postProcess(root);
        return root;
    }

    /**
     * Internal function to create the AST recursively from the concrete syntax tree.
     * This function should not do any transformations to the structure of the AST.
     * Only the nodes that are not relevant during the parsing process should be marked as hidden.
     * This ensures that the rhs modifications done during post processing are referencing the correct child node.
     *
     * @param concreteSyntaxTreeNode the current node
     * @return the node transformed to an ast node.
     */
    private static AbstractSyntaxTreeNode createRecursive(ConcreteSyntaxTreeNode concreteSyntaxTreeNode) {
        if(concreteSyntaxTreeNode.getRule() != null) {
            AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(concreteSyntaxTreeNode.getRule());

            for(int i = 0; i < concreteSyntaxTreeNode.getChildren().size(); i++) {
                ConcreteSyntaxTreeNode currentTreeNode = concreteSyntaxTreeNode.getChildren().get(i);
                AbstractSyntaxTreeNode childNode = createRecursive(currentTreeNode);
                node.addChild(childNode);
                childNode.setParent(node);
            }
            return node;
        } else {
            AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(concreteSyntaxTreeNode.getToken());
            if(!node.getToken().getKeepInAST()) {
                node.setHidden();
            }
            return node;
        }
    }

    /**
     * Post processes the AST.
     *
     * @param root the root node of the AST.
     */
    private static void postProcess(AbstractSyntaxTreeNode root) {
        if(root.getRule() != null) {
            List<List<SymbolModifier>> modifiers = root.getRule().getSymbolModifiers();
            Map<AbstractSyntaxTreeNode, List<SymbolModifier>> modifierMap = new HashMap<>();
            List<AbstractSyntaxTreeNode> children = root.getChildren();
            // Create a map that keeps maps the child nodes to their modifiers.
            // This is required as the rhs and lhs modifiers can modify the tree structure.
            // The modifiers are applied from the bottom up. First the modifiers are executed on the leaves
            // and at the end for the root node.
            for(int i = 0; i < children.size(); i++) {
                modifierMap.put(children.get(i), modifiers.get(i));
            }

            for(int i = 0; i < children.size(); i++) {
                AbstractSyntaxTreeNode child = children.get(i);
                postProcess(child);
                applyRhsModifiers(child, modifierMap.get(child));
            }

            applyLhsModifiers(root, root.getRule().getLeftHandModifiers());
        }
    }

    /**
     * Applies the left hand side modifiers of a production.
     *
     * @param node The node that should be modified
     * @param leftHandModifiers The modifiers that should be applied.
     */
    private static void applyLhsModifiers(AbstractSyntaxTreeNode node, List<SymbolModifier> leftHandModifiers) {
        for(SymbolModifier modifier: leftHandModifiers) {
            switch (modifier.getModifier()) {
                case SymbolModifier.LIST:
                    flattenList(node);
                    break;
            }
        }
    }

    /**
     * This function takes the left hand side of the current production and replaces all children that have the same
     * left side rule by all their children. This gets repeated until child can be replaced anymore.
     *
     * @param node The node where the children should be flattened.
     */
    private static void flattenList(AbstractSyntaxTreeNode node) {
        Symbol lhsSymbol = node.getRule().leftHandSymbol();

        boolean hasChanges = true;
        while(hasChanges) {
            hasChanges = false;
            for(AbstractSyntaxTreeNode child: node.getChildren()) {
                if(child.getRule() != null && child.getRule().leftHandSymbol().equals(lhsSymbol)) {
                    node.replaceChild(child, child.getAllChildren());
                    hasChanges = true;
                    break;
                }
            }
        }
    }

    /**
     * Applies all modifiers that exist on the right hand side of a production rule.
     *
     * @param node The current node that should be modified
     * @param modifiers The list of modifiers that are defined for this node on the parent.
     */
    private static void applyRhsModifiers(AbstractSyntaxTreeNode node, List<SymbolModifier> modifiers) {
        for(SymbolModifier modifier: modifiers) {
            switch (modifier.getModifier()) {
                case SymbolModifier.ALIAS:
                    String value = modifier.getValue();
                    node.setAlias(value);
                    break;
                case SymbolModifier.HIDDEN:
                    node.setHidden();
                    break;
                case SymbolModifier.INLINE:
                    node.getParent().replaceChild(node, node.getAllChildren());
                    break;
            }
        }
    }
}
