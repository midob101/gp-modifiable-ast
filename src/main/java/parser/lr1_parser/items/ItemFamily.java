package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;
import language_definitions.LanguageDefinition;
import logger.Logger;
import logger.LoggerComponents;
import parser.lr1_parser.Closure;
import parser.lr1_parser.Successor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ItemFamily {
    private final List<ItemSet> itemSets = new LinkedList<>();
    private ItemSet start;

    /**
     * Fills the item family based on a language definition.
     *
     * This works by creating first a dummy production S' -> .S, where . marks the position of the item.
     * This dummy production is placed into an item set. This item set is the start point.
     * The item set is decorated with the closure, afterwards all required successors are generated.
     * For each newly generated successor, again all required successors are generated. This repeats until no
     * new successors can be generated anymore.
     *
     * @param languageDefinition The language definition file.
     * @param successor The successor instance
     */
    public void create(LanguageDefinition languageDefinition, Successor successor) {
        List<GrammarRule> grammarRules = languageDefinition.getGrammarRules();
        Set<Symbol> allGrammarSymbols = languageDefinition.getAllGrammarSymbols();
        Symbol startSymbol = languageDefinition.getGrammarStartSymbol();
        Symbol startClone = Symbol.INTERNAL_START_COPY;
        Item initial = new Item(new GrammarRule(startClone, List.of(startSymbol)), 0, Symbol.END_OF_INPUT);
        start = new ItemSet();
        start.addItem(initial);
        Closure.decorateClosure(grammarRules, start);
        this.itemSets.add(start);

        Queue<ItemSet> toBeDone = new LinkedList<>();
        toBeDone.add(start);

        while(!toBeDone.isEmpty()) {
            ItemSet itemSet = toBeDone.poll();
            for(Symbol symbol : allGrammarSymbols) {
                ItemSet successorSet = successor.generateSuccessor(grammarRules, itemSet, symbol, this);
                if(!successorSet.isEmpty()) {
                    ItemSet existing = this.getSimilarItemSet(successorSet);
                    if(existing == null) {
                        Logger.debug(LoggerComponents.PARSER, "Adding new item set for " + itemSet + " and symbol " + symbol + " - " + successorSet);
                        this.itemSets.add(successorSet);
                        toBeDone.add(successorSet);
                    }
                }
            }
        }
    }

    /**
     * Gets the initial item set. This is the item set with the production S' -> .S (decorated with closure).
     *
     * @return The start item set
     */
    public ItemSet getStart() {
        return start;
    }

    /**
     * Gets all item sets.
     *
     * @return a list of all item sets in this family
     */
    public List<ItemSet> getItemSets() {
        return itemSets;
    }

    /**
     * Checks if a given item set is already present in this item family. This does not do a reference check, but checks
     * for equality of the items.
     *
     * @param itemSet the item set to check for
     * @return true if a similar item set is already present
     */
    public ItemSet getSimilarItemSet(ItemSet itemSet) {
        for(ItemSet itemSet1 : this.itemSets) {
            if(itemSet1.getItems().size() == itemSet.getItems().size()) {
                boolean isSimilar = itemSet.getItems().stream().allMatch(itemSet1::hasSimilarItem);
                if(isSimilar) {
                    return itemSet1;
                }
            }
        }
        return null;
    }
}
