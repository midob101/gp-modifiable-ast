package syntax_tree;

import grammar.GrammarRule;
import grammar.Symbol;
import grammar.SymbolModifier;
import java.util.List;

public class AbstractSyntaxTreeFactory {
    public static AbstractSyntaxTreeNode create(ConcreteSyntaxTreeNode concreteSyntaxTreeNode) {
        if(concreteSyntaxTreeNode.getRule() != null) {
            GrammarRule production = concreteSyntaxTreeNode.getRule();
            List<List<SymbolModifier>> allModifiers = production.getSymbolModifiers();
            List<Symbol> symbols = production.getSymbols();

            AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(concreteSyntaxTreeNode.getRule());
            int realIdx = 0;

            for(int i = 0; i < concreteSyntaxTreeNode.getChildren().size(); i++) {
                ConcreteSyntaxTreeNode currentTreeNode = concreteSyntaxTreeNode.getChildren().get(i);

                AbstractSyntaxTreeNode childNode = create(currentTreeNode);

                if(realIdx < allModifiers.size()) {
                    List<SymbolModifier> modifiers = allModifiers.get(realIdx);
                    if(modifiers.stream().anyMatch((mod) -> mod.getModifier().equals("hidden"))) {
                        childNode.setHidden();
                    }
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
}
