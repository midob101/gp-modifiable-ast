package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;
import parser.lr1_parser.items.Item;
import parser.lr1_parser.items.ItemFamily;
import parser.lr1_parser.items.ItemSet;

import java.util.HashMap;
import java.util.List;

public class Successor {

    private static final HashMap<ItemSet, HashMap<Symbol, ItemSet>> cache = new HashMap<>();

    public static void clearCache() {
        Successor.cache.clear();
    }

    public static ItemSet generateSuccessor(List<GrammarRule> g, ItemSet source, Symbol x, ItemFamily family) {
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
        ItemSet similar = family.getSimilarItemSet(successor);
        if(similar != null) {
            successor = similar;
        }
        HashMap<Symbol, ItemSet> map = cache.getOrDefault(source, new HashMap<>());
        map.put(x, successor);
        cache.put(source, map);
        return successor;
    }

    public static ItemSet getFromCache(ItemSet source, Symbol x) {
        if(cache.containsKey(source) && cache.get(source).containsKey(x)) {
            return cache.get(source).get(x);
        }
        return null;
    }
}
