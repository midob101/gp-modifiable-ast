package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;
import parser.lr1_parser.items.Item;
import parser.lr1_parser.items.ItemSet;

import java.util.HashMap;
import java.util.List;

public class Successor {

    /**
     * TODO: This implementation has issues, where the same item set can have seperate instances.
     * TODO: Item sets need to be unique. Duplicated instances of item sets with the same entries
     * TODO: will trigger errors.
     */
    private static HashMap<ItemSet, HashMap<Symbol, ItemSet>> cache = new HashMap<>();

    public static void clearCache() {
        Successor.cache.clear();
    }

    public static ItemSet generateSuccessor(List<GrammarRule> g, ItemSet source, Symbol x) {
        if(cache.containsKey(source) && cache.get(source).containsKey(x)) {
            return cache.get(source).get(x);
        }
        ItemSet successor = new ItemSet();
        for(Item item: source.getItems()) {
            if(!item.isPosAtEnd() && item.getSymbolAtPos().equals(x)) {
                successor.addItem(item.getNextItem());
            }
        }

        if(!successor.getItems().isEmpty()) {
            Closure.decorateClosure(g, successor);
        }
        HashMap<Symbol, ItemSet> map = cache.getOrDefault(source, new HashMap<>());
        map.put(x, successor);
        cache.put(source, map);
        return successor;
    }

    public static void setCachedValue(ItemSet source, Symbol x, ItemSet successor) {
        cache.get(source).put(x, successor);
    }

    public static ItemSet getFromCache(ItemSet source, Symbol x) {
        if(cache.containsKey(source) && cache.get(source).containsKey(x)) {
            return cache.get(source).get(x);
        }
        return null;
    }
}
