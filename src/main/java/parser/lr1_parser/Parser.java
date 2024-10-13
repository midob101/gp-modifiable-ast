package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;
import language_definitions.LanguageDefinition;
import lexer.Token;
import lexer.TokenList;
import logger.Logger;
import logger.LoggerComponents;
import parser.lr1_parser.action_table.*;
import parser.lr1_parser.action_table.BaseAction;
import parser.lr1_parser.items.ItemFamily;
import parser.lr1_parser.items.ItemSet;

import java.util.Stack;

public class Parser {

    public static void clearCache() {
        FirstSet.clearCache();
        Successor.clearCache();
    }

    /**
     * Checks if a given token list matches the productions of a language definition.
     *
     * @param tokenList The token list from the lexer process
     * @param languageDefinition The language definition for which the productions are used.
     * @return the valid flag
     */
    public static boolean isValid(TokenList tokenList, LanguageDefinition languageDefinition) {
        ItemFamily itemFamily = new ItemFamily();
        itemFamily.create(languageDefinition);

        ActionTable actionTable = ActionTable.createFromFamily(itemFamily);
        GotoTable gotoTable = GotoTable.createFromFamily(languageDefinition.getAllNonTerminalSymbols(), itemFamily);

        Stack<ItemSet> stack = new Stack<>();
        stack.push(itemFamily.getStart());

        TokenConsumer tokenConsumer = new TokenConsumer(tokenList);
        Symbol next = tokenConsumer.consume();
        Token nextToken = tokenConsumer.consumeToken();

        while(true) {
            ItemSet currentTopOfStack = stack.peek();
            BaseAction action = actionTable.getAction(currentTopOfStack, next);
            if(action != null) {
                if(action.getClass() == ShiftAction.class) {
                    ShiftAction shiftAction = (ShiftAction) action;
                    ItemSet shiftTo = shiftAction.getShiftTo();
                    stack.push(shiftTo);
                    next = tokenConsumer.consume();
                    nextToken = tokenConsumer.consumeToken();
                    Logger.debug(LoggerComponents.PARSER, "Shifting new state onto stack");
                } else if(action.getClass() == ReduceAction.class) {
                    ReduceAction reduceAction = (ReduceAction) action;
                    GrammarRule rule = reduceAction.getReducedRule();
                    Symbol s = rule.leftHandSymbol();
                    for(int i = 0; i < rule.symbols().size(); i++) {
                        stack.pop();
                    }
                    currentTopOfStack = stack.peek();
                    stack.push(gotoTable.getTarget(currentTopOfStack, s));
                    Logger.debug(LoggerComponents.PARSER, "Reducing grammar rule " + rule);
                } else if(action.getClass() == AcceptAction.class) {
                    return true;
                }
            } else {
                Logger.err(LoggerComponents.PARSER, "Next Token: " + (nextToken != null ? nextToken.toString() : "undefined"));
                Logger.err(LoggerComponents.PARSER, "Expected one of: " + actionTable.getExpectedSymbols(currentTopOfStack).toString());
                Logger.err(LoggerComponents.PARSER, "Invalid source, does not match the grammar definitions.");
                return false;
            }
        }
    }
}
