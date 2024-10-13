package syntax_tree;

import grammar.GrammarRule;
import grammar.SymbolModifier;
import java.util.List;

public class AbstractSyntaxTreeFactory {
    public static AbstractSyntaxTreeNode create(ConcreteSyntaxTreeNode concreteSyntaxTreeNode) {
        if(concreteSyntaxTreeNode.getRule() != null) {
            GrammarRule production = concreteSyntaxTreeNode.getRule();
            List<List<SymbolModifier>> allModifiers = production.getSymbolModifiers();

            AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(concreteSyntaxTreeNode.getRule());
            int realIdx = 0;

            for(int i = 0; i < concreteSyntaxTreeNode.getChildren().size(); i++) {
                ConcreteSyntaxTreeNode currentTreeNode = concreteSyntaxTreeNode.getChildren().get(i);

                AbstractSyntaxTreeNode childNode = create(currentTreeNode);

                if(realIdx < allModifiers.size()) {
                    applyModifiers(childNode, allModifiers.get(realIdx));
                }
                if(currentTreeNode.getRule() != null || currentTreeNode.getToken().getKeepInAST()) {
                    realIdx++;
                }

                node.addChild(childNode);
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

    private static void applyModifiers(AbstractSyntaxTreeNode node, List<SymbolModifier> modifiers) {
        for(SymbolModifier modifier: modifiers) {
            switch (modifier.getModifier()) {
                case SymbolModifier.ALIAS:
                    String value = modifier.getValue();
                    node.setAlias(value);
                    break;
                case SymbolModifier.HIDDEN:
                    node.setHidden();
                    break;
            }
        }
    }
}
