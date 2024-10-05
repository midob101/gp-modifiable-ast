package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;
import language_definitions.LanguageDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ItemFamily {
    private List<ItemSet> itemSets = new LinkedList<>();

    public void create(LanguageDefinition languageDefinition) {
        List<GrammarRule> grammarRules = languageDefinition.getGrammarRules();
        Set<Symbol> allGrammarSymbols = languageDefinition.getAllGrammarSymbols();
        Symbol start = languageDefinition.getGrammarStartSymbol();
        Symbol startClone = new Symbol("INTERNAL_START_COPY", true);
        Item initial = new Item(new GrammarRule(startClone, List.of(start)), 0, Symbol.END_OF_INPUT);
        ItemSet initialSet = new ItemSet();
        initialSet.addItem(initial);
        Closure.decorateClosure(grammarRules, initialSet);
        this.itemSets.add(initialSet);

        boolean changed = true;

        while(changed) {
            changed = false;
            List<ItemSet> itemSetsToBeAdded = new LinkedList<>();
            for(ItemSet itemSet : itemSets) {
                for(Symbol symbol : allGrammarSymbols) {
                    ItemSet successor = Successor.generateSuccessor(grammarRules, itemSet, symbol);
                    if(!successor.isEmpty() && !this.hasSimilarItemSet(successor)) {
                        itemSetsToBeAdded.add(successor);
                        changed = true;
                    }
                }
            }

            this.itemSets.addAll(itemSetsToBeAdded);
        }
    }

    public List<ItemSet> getItemSets() {
        return itemSets;
    }

    private boolean hasSimilarItemSet(ItemSet itemSet) {
        for(ItemSet itemSet1 : this.itemSets) {
            if(itemSet1.getItems().size() == itemSet.getItems().size()) {
                boolean isSimilar = itemSet.getItems().stream().allMatch(itemSet1::hasSimilarItem);
                if(isSimilar) {
                    return true;
                }
            }
        }
        return false;
    }
}
