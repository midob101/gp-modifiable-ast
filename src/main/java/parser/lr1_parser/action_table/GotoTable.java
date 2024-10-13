package parser.lr1_parser.action_table;

import grammar.Symbol;
import parser.lr1_parser.items.ItemFamily;
import parser.lr1_parser.items.ItemSet;
import parser.lr1_parser.Successor;

import java.util.HashMap;
import java.util.Set;

/**
 * This class keeps track of all the goto actions.
 * This is not done by a two-dimensional array, instead it keeps track of the available entries through hashmaps.
 */
public class GotoTable {
    private final HashMap<ItemSet, HashMap<Symbol, ItemSet>> gotoMap = new HashMap<>();

    /**
     * Defines a goto target of an item set and symbol
     *
     * @param itemSet The source item set
     * @param s the symbol that would trigger the goto
     * @param target the target item set
     */
    public void setTarget(ItemSet itemSet, Symbol s, ItemSet target) {
        if(s.isTerminal()) {
            throw new RuntimeException("setAction can only be called on a non terminal symbol");
        }

        if(!gotoMap.containsKey(itemSet)) {
            gotoMap.put(itemSet, new HashMap<>());
        }

        HashMap<Symbol, ItemSet> actions = gotoMap.get(itemSet);
        actions.put(s, target);
    }

    /**
     * Gets the goto target
     *
     * @param itemSet the source item set
     * @param s the symbol that triggers the goto
     * @return the target item set
     */
    public ItemSet getTarget(ItemSet itemSet, Symbol s) {
        if(s.isTerminal()) {
            throw new RuntimeException("setAction can only be called on a non terminal symbol");
        }

        return gotoMap.get(itemSet).get(s);
    }

    /**
     * Creates a new goto table based on an item family
     *
     * @param nonTerminals all non-terminal symbols of the language grammar
     * @param family the complete item family
     * @param successor the successor instance
     * @return the generated goto table
     */
    public static GotoTable createFromFamily(Set<Symbol> nonTerminals, ItemFamily family, Successor successor) {
        GotoTable gotoTable = new GotoTable();

        for(ItemSet itemSet: family.getItemSets()) {
            for(Symbol nonTerminal: nonTerminals) {
                ItemSet gotoTarget = successor.getFromCache(itemSet, nonTerminal);
                if(gotoTarget != null) {
                    gotoTable.setTarget(itemSet, nonTerminal, gotoTarget);
                }
            }
        }

        return gotoTable;
    }
}
