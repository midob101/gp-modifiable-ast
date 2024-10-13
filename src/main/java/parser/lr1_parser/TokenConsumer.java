package parser.lr1_parser;

import grammar.Symbol;
import lexer.Token;
import lexer.TokenList;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Simple class to consume the token list from the lexer for the parsing process.
 */
public class TokenConsumer {
    Queue<Symbol> symbols = new LinkedList<>();
    Queue<Token> tokens = new LinkedList<>();

    public TokenConsumer(TokenList tokenList) {
        for(Token t: tokenList.getIterable()) {
            if(t.getKeepInAST()) {
                symbols.add(new Symbol(t.getLexerDefinition().getName(), true));
                tokens.add(t);
            } else {
                symbols.add(Symbol.IGNORE_IN_PARSE);
                tokens.add(t);
            }
        }
        symbols.add(Symbol.END_OF_INPUT);
    }

    public Symbol consume() {
        return symbols.poll();
    }

    public Token consumeToken() {
        return tokens.poll();
    }
}
