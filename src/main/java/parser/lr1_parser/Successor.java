package parser.lr1_parser;

import grammar.Production;
import grammar.Symbol;
import parser.lr1_parser.items.Item;
import parser.lr1_parser.items.ItemFamily;
import parser.lr1_parser.items.ItemSet;

import java.util.HashMap;
import java.util.List;

/**
 * Calculates the successor set
 */
public class Successor {

    private final HashMap<ItemSet, HashMap<Symbol, ItemSet>> cache = new HashMap<>();

    /**
     * Generates the successor item set for a given item set and a symbol.
     *
     * @param g The list of productions
     * @param source The original item set to calculate the successor for
     * @param x The symbol the successor should be calculated for
     * @param family the entire item family. required to find identical item sets to the generated one
     * @return The successor item set
     */
    public ItemSet generateSuccessor(List<Production> g, ItemSet source, Symbol x, ItemFamily family) {
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

    /**
     * Receives a cached successor set from cache. This function will not generate a new one if it is not already present
     * in the cache. Before calling this function, you should make sure that this item has been calculated already.
     *
     * @param source The original item set to calculate the successor for
     * @param x The symbol the successor should be calculated for
     * @return the cached successor
     */
    public ItemSet getFromCache(ItemSet source, Symbol x) {
        if(cache.containsKey(source) && cache.get(source).containsKey(x)) {
            return cache.get(source).get(x);
        }
        return null;
    }
}
