package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;

import java.util.List;

public class Successor {
    public static ItemSet generateSuccessor(List<GrammarRule> g, ItemSet source, Symbol x) {
        ItemSet successor = new ItemSet();
        for(Item item: source.getItems()) {
            if(!item.isPosAtEnd() && item.getSymbolAtPos().equals(x)) {
                successor.addItem(item.getNextItem());
            }
        }

        if(!successor.getItems().isEmpty()) {
            Closure.decorateClosure(g, successor);
        }
        return successor;
    }
}
