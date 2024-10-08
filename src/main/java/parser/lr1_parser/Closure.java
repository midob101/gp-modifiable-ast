package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;
import parser.lr1_parser.items.Item;
import parser.lr1_parser.items.ItemSet;

import java.util.LinkedList;
import java.util.List;

public class Closure {
    /**
     * Decorates an item set by the relevant grammar rules for this set.
     *
     * Algorithm:
     *      For each item A -> u.Bv, a:
     *          For each production B -> w:
     *              For each terminal b in FIRST(va):
     *                  If (B -> .w, b) is not in the item set
     *                      Add (B -> .w, b) to the item set
     *
     *      Repeat until no items are added.
     *
     * This follows the algorithm defined in the following lecture (page 6).
     * https://web.stanford.edu/class/archive/cs/cs143/cs143.1128/handouts/110%20LR%20and%20SLR%20Parsing.pdf
     */
    public static void decorateClosure(List<GrammarRule> grammarRules, ItemSet itemSet) {
        boolean hasChanges = true;
        while (hasChanges) {
            hasChanges = false;
            // Keep track of the items to be added in a seperate list.
            // This is only done to prevent modifying the list we are iterating over.
            LinkedList<Item> addedItems = new LinkedList<>();
            for (Item item : itemSet.getItems()) {
                if(!item.isPosAtEnd() && !item.getSymbolAtPos().isTerminal()) {
                    for(GrammarRule production: grammarRules) {
                        if(production.leftHandSymbol().equals(item.getSymbolAtPos())) {
                            List<Symbol> calculateFirstFor = item.getRemainingSymbols();
                            calculateFirstFor.add(item.getLookaheadSymbol());
                            FirstSet first = FirstSet.generate(grammarRules, calculateFirstFor);
                            for(Symbol terminal: first.getSymbolList()) {
                                addedItems.add(new Item(production, 0, terminal));
                            }
                        }
                    }
                }
            }
            // Adding the queued items to the item set.
            for(Item addedItem: addedItems) {
                if(!itemSet.hasSimilarItem(addedItem)) {
                    itemSet.addItem(addedItem);
                    hasChanges = true;
                }
            }
        }
    }
}
