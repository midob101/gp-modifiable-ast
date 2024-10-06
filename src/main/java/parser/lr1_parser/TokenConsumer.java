package parser.lr1_parser;

import grammar.Symbol;
import lexer.Token;
import lexer.TokenList;

import java.util.LinkedList;
import java.util.Queue;

public class TokenConsumer {
    Queue<Symbol> symbols = new LinkedList<>();

    public TokenConsumer(TokenList tokenList) {
        for(Token t: tokenList.getIterable()) {
            symbols.add(new Symbol(t.getLexerDefinition().getName(), true));
        }
        symbols.add(Symbol.END_OF_INPUT);
    }

    public Symbol consume() {
        return symbols.poll();
    }
}
