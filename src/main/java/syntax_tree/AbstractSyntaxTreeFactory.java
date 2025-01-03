package syntax_tree;

import grammar.Symbol;
import grammar.SymbolModifier;
import syntax_tree.ast.AbstractSyntaxTreeNode;
import syntax_tree.ast.ProductionTreeNode;
import syntax_tree.ast.TokenTreeNode;
import syntax_tree.ast.exceptions.AddingConnectedNode;
import syntax_tree.ast.exceptions.ReplacingNonChildNode;

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
        if(concreteSyntaxTreeNode.getProduction() != null) {
            AbstractSyntaxTreeNode node = new ProductionTreeNode(concreteSyntaxTreeNode.getProduction());

            for(int i = 0; i < concreteSyntaxTreeNode.getChildren().size(); i++) {
                ConcreteSyntaxTreeNode currentTreeNode = concreteSyntaxTreeNode.getChildren().get(i);
                AbstractSyntaxTreeNode childNode = createRecursive(currentTreeNode);
                try {
                    node.addChild(childNode);
                } catch (AddingConnectedNode e) {
                    // Should never happen, the child node was just created.
                    throw new RuntimeException(e);
                }
            }
            return node;
        } else {
            AbstractSyntaxTreeNode node = new TokenTreeNode(concreteSyntaxTreeNode.getToken());
            if(!concreteSyntaxTreeNode.getToken().getKeepInAST()) {
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
        if(root instanceof ProductionTreeNode productionTreeNode) {
            List<List<SymbolModifier>> modifiers = productionTreeNode.getProduction().getSymbolModifiers();
            Map<AbstractSyntaxTreeNode, List<SymbolModifier>> modifierMap = new HashMap<>();
            List<AbstractSyntaxTreeNode> children = productionTreeNode.getChildren();
            // Create a map that keeps maps the child nodes to their modifiers.
            // This is required as the rhs and lhs modifiers can modify the tree structure.
            // The modifiers are applied from the bottom up. First the modifiers are executed on the leaves
            // and at the end for the root node.
            for(int i = 0; i < children.size(); i++) {
                modifierMap.put(children.get(i), modifiers.get(i));
            }

            for (AbstractSyntaxTreeNode child : children) {
                postProcess(child);
                applyRhsModifiers(child, modifierMap.get(child));
            }

            applyLhsModifiers(productionTreeNode, productionTreeNode.getProduction().getLeftHandModifiers());
        }
    }

    /**
     * Applies the left hand side modifiers of a production.
     *
     * @param node The node that should be modified
     * @param leftHandModifiers The modifiers that should be applied.
     */
    private static void applyLhsModifiers(ProductionTreeNode node, List<SymbolModifier> leftHandModifiers) {
        for(SymbolModifier modifier: leftHandModifiers) {
            switch (modifier.getModifier()) {
                case SymbolModifier.LIST:
                    flattenList(node);
                    break;
            }
        }
    }

    /**
     * This function takes the left hand symbol of the current production and replaces all children that have the same
     * left side symbol by all their children. This gets repeated until child can be replaced anymore.
     *
     * @param node The node where the children should be flattened.
     */
    private static void flattenList(ProductionTreeNode node) {
        Symbol lhsSymbol = node.getProduction().leftHandSymbol();

        boolean hasChanges = true;
        while(hasChanges) {
            hasChanges = false;
            for(AbstractSyntaxTreeNode child: node.getChildren()) {
                if(child instanceof ProductionTreeNode childTmp) {
                    if (childTmp.getProduction().leftHandSymbol().equals(lhsSymbol)) {
                        try {
                            node.replaceChild(childTmp, childTmp.getAllChildren(), true);
                        } catch (ReplacingNonChildNode|AddingConnectedNode e) {
                            // We can safely ignore the exception at this point. We know, that the child is a direct
                            // child of the node and has no other parent.
                            throw new RuntimeException(e);
                        }
                        hasChanges = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Applies all modifiers that exist on the right hand side of a production.
     *
     * @param node The current node that should be modified
     * @param modifiers The list of modifiers that are defined for this node on the parent.
     */
    private static void applyRhsModifiers(AbstractSyntaxTreeNode node, List<SymbolModifier> modifiers) {
        // TODO: Verify the modifiers are applied to correct nodes.

        // Apply alias modifier first.
        for(SymbolModifier modifier: modifiers) {
            switch (modifier.getModifier()) {
                case SymbolModifier.ALIAS:
                    String value = modifier.getValue();
                    node.addAlias(value);
                    break;
            }
        }
        for(SymbolModifier modifier: modifiers) {
            switch (modifier.getModifier()) {
                case SymbolModifier.HIDDEN:
                    node.setHidden();
                    break;
                case SymbolModifier.INLINE:
                    try {
                        // Carry aliases down to all children
                        for(AbstractSyntaxTreeNode child: node.getAllChildren()) {
                            child.addAliases(node.getAliases());
                        }
                        node.getParent().replaceChild(node, node.getAllChildren(), true);
                    } catch (ReplacingNonChildNode|AddingConnectedNode e) {
                        // We can safely ignore the exception at this point. We know, that the child is a direct
                        // child of the node.
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    }
}
