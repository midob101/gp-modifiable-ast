package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;
import language_definitions.LanguageDefinition;
import lexer.TokenList;
import logger.Logger;
import logger.LoggerComponents;

import java.util.Stack;

public class Parser {

    public static boolean isValid(TokenList tokenList, LanguageDefinition languageDefinition) {
        ItemFamily itemFamily = new ItemFamily();
        itemFamily.create(languageDefinition);

        ActionTable actionTable = ActionTable.createFromFamily(itemFamily);
        GotoTable gotoTable = GotoTable.createFromFamily(languageDefinition.getAllNonTerminalSymbols(), itemFamily);

        Stack<ItemSet> stack = new Stack<>();
        stack.push(itemFamily.getStart());

        TokenConsumer tokenConsumer = new TokenConsumer(tokenList);
        Symbol next = tokenConsumer.consume();

        while(true) {
            ItemSet currentTopOfStack = stack.peek();
            BaseAction action = actionTable.getAction(currentTopOfStack, next);
            if(action != null) {
                if(action.getClass() == ShiftAction.class) {
                    ShiftAction shiftAction = (ShiftAction) action;
                    ItemSet shiftTo = shiftAction.getShiftTo();
                    stack.push(shiftTo);
                    next = tokenConsumer.consume();
                } else if(action.getClass() == ReduceAction.class) {
                    ReduceAction reduceAction = (ReduceAction) action;
                    GrammarRule rule = reduceAction.getReducedRule();
                    Symbol s = rule.getLeftHandSymbol();
                    for(int i = 0; i < rule.getSymbols().size(); i++) {
                        stack.pop();
                    }
                    currentTopOfStack = stack.peek();
                    stack.push(gotoTable.getTarget(currentTopOfStack, s));
                } else if(action.getClass() == AcceptAction.class) {
                    return true;
                }
            } else {
                Logger.err(LoggerComponents.PARSER, "Invalid source, does not match the grammar definitions.");
                return false;
            }
        }
    }
}
