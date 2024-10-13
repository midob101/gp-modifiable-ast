package parser.lr1_parser;

import syntax_tree.ConcreteSyntaxTreeNode;
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

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    private final ItemFamily itemFamily = new ItemFamily();
    private ActionTable actionTable = null;
    private GotoTable gotoTable = null;

    /**
     * Creates the item family, the action table and the goto table for the parser.
     * @param languageDefinition The language definition for which the parser should work
     */
    public void initialize(LanguageDefinition languageDefinition) {
        Successor successor = new Successor();

        itemFamily.create(languageDefinition, successor);
        actionTable = ActionTable.createFromFamily(itemFamily, successor);
        gotoTable = GotoTable.createFromFamily(languageDefinition.getAllNonTerminalSymbols(), itemFamily, successor);
    }

    /**
     * Checks if a given token list matches the productions of a language definition.
     *
     * @param tokenList The token list from the lexer process
     * @return the valid flag
     */
    public boolean isValid(TokenList tokenList) {
        ConcreteSyntaxTreeNode tree = this.createCST(tokenList);
        return tree != null;
    }

    /**
     * Creates the concrete syntax tree from a given token list.
     *
     * @param tokenList The token list from the lexer process
     * @return The root node of the concrete syntax tree
     */
    public ConcreteSyntaxTreeNode createCST(TokenList tokenList) {
        Stack<ItemSet> stack = new Stack<>();
        stack.push(itemFamily.getStart());

        TokenConsumer tokenConsumer = new TokenConsumer(tokenList);
        Symbol next = tokenConsumer.consume();
        Token nextToken = tokenConsumer.consumeToken();

        LinkedList<ConcreteSyntaxTreeNode> danglingTreeNodes = new LinkedList<>();

        while(true) {
            if (next.equals(Symbol.IGNORE_IN_PARSE)) {
                // This node is not part of the grammar. Create a tree node and continue.
                danglingTreeNodes.add(new ConcreteSyntaxTreeNode(nextToken));
                next = tokenConsumer.consume();
                nextToken = tokenConsumer.consumeToken();
            } else {
                // This node is part of the grammar. Get the action from the action table and perform the
                // defined action for this node.
                ItemSet currentTopOfStack = stack.peek();
                BaseAction action = actionTable.getAction(currentTopOfStack, next);
                if (action != null) {
                    if (action.getClass() == ShiftAction.class) {
                        // Read the next token from the input and shift to a new state.
                        ShiftAction shiftAction = (ShiftAction) action;
                        ItemSet shiftTo = shiftAction.getShiftTo();
                        stack.push(shiftTo);
                        danglingTreeNodes.add(new ConcreteSyntaxTreeNode(nextToken));
                        next = tokenConsumer.consume();
                        nextToken = tokenConsumer.consumeToken();
                        Logger.debug(LoggerComponents.PARSER, "Shifting new state onto stack " + (nextToken != null ? nextToken.toString() : ""));
                    } else if (action.getClass() == ReduceAction.class) {
                        // Reduce the production matched.
                        ReduceAction reduceAction = (ReduceAction) action;
                        GrammarRule rule = reduceAction.getReducedRule();
                        Symbol s = rule.leftHandSymbol();
                        ConcreteSyntaxTreeNode newNode = new ConcreteSyntaxTreeNode(rule);
                        int remaining = rule.getSymbols().size();
                        for (int i = 0; i < rule.getSymbols().size(); i++) {
                            stack.pop();
                            ConcreteSyntaxTreeNode child = danglingTreeNodes.removeLast();
                            newNode.addChild(child);
                            if (child.getToken() == null || child.getToken().getKeepInAST()) {
                                remaining--;
                            }
                        }
                        // In case there were internal parse irrelevant nodes on the danglingTreeNodes stack, we need to remove
                        // enough elements from this list until we have removed exactly as many non parse irrelevant nodes
                        // as there are symbols on the right hand side of the production.
                        while (remaining != 0) {
                            ConcreteSyntaxTreeNode child = danglingTreeNodes.removeLast();
                            newNode.addChild(child);
                            if (child.getToken() == null || child.getToken().getKeepInAST()) {
                                remaining--;
                            }
                        }
                        currentTopOfStack = stack.peek();
                        stack.push(gotoTable.getTarget(currentTopOfStack, s));

                        danglingTreeNodes.add(newNode);
                        Logger.debug(LoggerComponents.PARSER, "Reducing grammar rule " + rule);
                    } else if (action.getClass() == AcceptAction.class) {
                        return getRootNode(danglingTreeNodes);
                    }
                } else {
                    Logger.err(LoggerComponents.PARSER, "Next Token: " + (nextToken != null ? nextToken.toString() : "undefined"));
                    Logger.err(LoggerComponents.PARSER, "Expected one of: " + actionTable.getExpectedSymbols(currentTopOfStack).toString());
                    Logger.err(LoggerComponents.PARSER, "Invalid source, does not match the grammar definitions.");
                    return null;
                }
            }
        }
    }

    /**
     * Given a list of tree nodes, this function collapses them into one single root node.
     * For this there should only be exactly one node in the list that is not a INTERNAL_PARSE_IRRELEVANT_NAME node.
     *
     * This is required as INTERNAL_PARSE_IRRELEVANT_NAME nodes would otherwise not be part of the CST if they are
     * before the start node of the grammar in the file.
     *
     * @param danglingTreeNodes the list of the remaining tree nodes
     * @return the main node
     */
    private ConcreteSyntaxTreeNode getRootNode(LinkedList<ConcreteSyntaxTreeNode> danglingTreeNodes) {
        ConcreteSyntaxTreeNode rootNode = null;
        for(ConcreteSyntaxTreeNode node: danglingTreeNodes) {
            if(node.getRule() != null) {
                rootNode = node;
                break;
            }
        }
        if(rootNode == null) {
            throw new RuntimeException("No main node was detected after the parsing process");
        }
        danglingTreeNodes.remove(rootNode);
        for(ConcreteSyntaxTreeNode node: danglingTreeNodes) {
            rootNode.addChild(node);
        }
        return rootNode;
    }
}
