package parser.lr1_parser.action_table;

import grammar.Symbol;
import parser.lr1_parser.items.ItemFamily;
import parser.lr1_parser.items.ItemSet;
import parser.lr1_parser.Successor;

import java.util.HashMap;
import java.util.Set;

public class GotoTable {
    private final HashMap<ItemSet, HashMap<Symbol, ItemSet>> gotoMap = new HashMap<ItemSet, HashMap<Symbol, ItemSet>>();

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

    public ItemSet getTarget(ItemSet itemSet, Symbol s) {
        if(s.isTerminal()) {
            throw new RuntimeException("setAction can only be called on a non terminal symbol");
        }

        return gotoMap.get(itemSet).get(s);
    }

    public static GotoTable createFromFamily(Set<Symbol> nonTerminals, ItemFamily family) {
        GotoTable gotoTable = new GotoTable();

        for(ItemSet itemSet: family.getItemSets()) {
            for(Symbol nonTerminal: nonTerminals) {
                ItemSet gotoTarget = Successor.getFromCache(itemSet, nonTerminal);
                if(gotoTarget != null) {
                    gotoTable.setTarget(itemSet, nonTerminal, gotoTarget);
                }
            }
        }

        return gotoTable;
    }
}
