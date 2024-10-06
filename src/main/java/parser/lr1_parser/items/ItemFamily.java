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
    private List<ItemSet> itemSets = new LinkedList<>();
    private ItemSet start;

    public void create(LanguageDefinition languageDefinition) {
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
                ItemSet successor = Successor.generateSuccessor(grammarRules, itemSet, symbol);
                if(!successor.isEmpty()) {
                    // TODO: Reformat this piece of code.
                    // The item family should not be responsible to update the cache of the successor.
                    ItemSet existing = this.getSimilarItemSet(successor);
                    if(existing == null) {
                        Logger.debug(LoggerComponents.PARSER, "Adding new item set for " + itemSet + " and symbol " + symbol + " - " + successor);
                        this.itemSets.add(successor);
                        toBeDone.add(successor);
                    } else {
                        Logger.debug(LoggerComponents.PARSER, "Item set " + itemSet + " and symbol " + symbol + " has existing item set as reference " + existing);
                        Successor.setCachedValue(itemSet, symbol, existing);
                    }
                }
            }
        }
    }

    public ItemSet getStart() {
        return start;
    }

    public List<ItemSet> getItemSets() {
        return itemSets;
    }

    private ItemSet getSimilarItemSet(ItemSet itemSet) {
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
