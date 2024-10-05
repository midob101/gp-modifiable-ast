package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;
import language_definitions.LanguageDefinition;

import java.util.LinkedList;
import java.util.List;
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

        boolean changed = true;

        while(changed) {
            changed = false;
            List<ItemSet> itemSetsToBeAdded = new LinkedList<>();
            for(ItemSet itemSet : itemSets) {
                for(Symbol symbol : allGrammarSymbols) {
                    ItemSet successor = Successor.generateSuccessor(grammarRules, itemSet, symbol);
                    if(!successor.isEmpty()) {
                        // TODO: Reformat this piece of code.
                        // The item family should not be responsible to update the cache of the successor.
                        ItemSet existing = this.getSimilarItemSet(successor);
                        if(existing == null) {
                            itemSetsToBeAdded.add(successor);
                            changed = true;
                        } else {
                            Successor.setCachedValue(itemSet, symbol, existing);
                        }
                    }
                }
            }

            this.itemSets.addAll(itemSetsToBeAdded);
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
