package parser.lr1_parser.action_table;

import grammar.Symbol;
import logger.Logger;
import logger.LoggerComponents;
import parser.lr1_parser.Successor;
import parser.lr1_parser.items.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * This class keeps track of all the shift, reduce and accept actions.
 * This is not done by a two-dimensional array, instead it keeps track of the available entries through hashmaps.
 */
public class ActionTable {
    private final HashMap<ItemSet, HashMap<Symbol, BaseAction>> actionMap = new LinkedHashMap<>();

    /**
     * Sets an action for a given item set and symbol.
     * This function can throw if there is either a shift/reduce or a reduce/reduce conflict.
     *
     * @param itemSet The source item set
     * @param s The symbol that should trigger a reduce or shift
     * @param action Either a shift, reduce or accept action.
     */
    public void setAction(ItemSet itemSet, Symbol s, BaseAction action) {
        if(!s.isTerminal()) {
            throw new RuntimeException("setAction can only be called on a terminal symbol");
        }

        if(!actionMap.containsKey(itemSet)) {
            actionMap.put(itemSet, new HashMap<>());
        }

        HashMap<Symbol, BaseAction> actions = actionMap.get(itemSet);
        if(actions.containsKey(s) && !actions.get(s).equals(action)) {
            Logger.err(LoggerComponents.PARSER, "Symbol: " + s);
            Logger.err(LoggerComponents.PARSER, "Existing action: " + actions.get(s).toString());
            Logger.err(LoggerComponents.PARSER, "New action: " + action.toString());
            throw new RuntimeException("An action for the key " + s + " already exists for the given item set.");
        }
        actions.put(s, action);
    }

    /**
     * Gets an action for a given non-terminal symbol and source item set
     *
     * @param itemSet The source item set
     * @param s The symbol that should trigger a reduce or shift
     * @return The action defined for this item set and symbol
     */
    public BaseAction getAction(ItemSet itemSet, Symbol s) {
        if(!s.isTerminal()) {
            throw new RuntimeException("getAction can only be called on a terminal symbol");
        }

        if(!actionMap.containsKey(itemSet)) {
            throw new RuntimeException("getAction called on non existing item set");
        }

        return actionMap.get(itemSet).get(s);
    }

    /**
     * Gets a set of symbol that would be expected for a given item set.
     *
     * @param itemSet The source item set
     * @return A set of symbols that are defined for this item set
     */
    public Set<Symbol> getExpectedSymbols(ItemSet itemSet) {
        return actionMap.get(itemSet).keySet();
    }

    /**
     * Creates a new action table based on a family
     *
     * @param family The complete item family.
     * @param successor The successor instance
     * @return The full action table
     */
    public static ActionTable createFromFamily(ItemFamily family, Successor successor) {
        ActionTable actionTable = new ActionTable();

        for(ItemSet itemSet: family.getItemSets()) {
            for(Item item: itemSet.getItems()) {
                if(item.isPosAtEnd() && !item.production().leftHandSymbol().equals(Symbol.INTERNAL_START_COPY)) {
                    actionTable.setAction(itemSet, item.lookaheadSymbol(), new ReduceAction(item.production()));
                }
                if(item.isPosAtEnd() && item.production().leftHandSymbol().equals(Symbol.INTERNAL_START_COPY)) {
                    actionTable.setAction(itemSet, item.lookaheadSymbol(), new AcceptAction());
                }
                if(!item.isPosAtEnd() && item.getSymbolAtPos().isTerminal()) {
                    ItemSet shiftTarget = successor.getFromCache(itemSet, item.getSymbolAtPos());
                    if(shiftTarget != null) {
                        actionTable.setAction(itemSet, item.getSymbolAtPos(), new ShiftAction(shiftTarget));
                    }
                }
            }
        }

        return actionTable;
    }
}
