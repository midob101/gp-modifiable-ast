package parser.lr1_parser.action_table;

import grammar.Symbol;
import logger.Logger;
import logger.LoggerComponents;
import parser.lr1_parser.Successor;
import parser.lr1_parser.items.*;

import java.util.HashMap;
import java.util.Set;

public class ActionTable {
    private final HashMap<ItemSet, HashMap<Symbol, BaseAction>> actionMap = new HashMap<ItemSet, HashMap<Symbol, BaseAction>>();

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

    public BaseAction getAction(ItemSet itemSet, Symbol s) {
        if(!s.isTerminal()) {
            throw new RuntimeException("getAction can only be called on a terminal symbol");
        }

        if(!actionMap.containsKey(itemSet)) {
            throw new RuntimeException("getAction called on non existing item set");
        }

        return actionMap.get(itemSet).get(s);
    }

    public Set<Symbol> getExpectedSymbols(ItemSet itemSet) {
        return actionMap.get(itemSet).keySet();
    }

    public static ActionTable createFromFamily(ItemFamily family) {
        ActionTable actionTable = new ActionTable();

        for(ItemSet itemSet : family.getItemSets()) {
            for(Item item: itemSet.getItems()) {
                if(item.isPosAtEnd() && !item.getGrammarRule().leftHandSymbol().equals(Symbol.INTERNAL_START_COPY)) {
                    actionTable.setAction(itemSet, item.getLookaheadSymbol(), new ReduceAction(item.getGrammarRule()));
                }
                if(item.isPosAtEnd() && item.getGrammarRule().leftHandSymbol().equals(Symbol.INTERNAL_START_COPY)) {
                    actionTable.setAction(itemSet, item.getLookaheadSymbol(), new AcceptAction());
                }
                if(!item.isPosAtEnd() && item.getSymbolAtPos().isTerminal()) {
                    ItemSet shiftTarget = Successor.getFromCache(itemSet, item.getSymbolAtPos());
                    if(shiftTarget != null) {
                        actionTable.setAction(itemSet, item.getSymbolAtPos(), new ShiftAction(shiftTarget));
                    }
                }
            }
        }

        return actionTable;
    }
}
