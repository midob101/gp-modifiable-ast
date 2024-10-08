package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Item {
    private final GrammarRule grammarRule;
    private final int pos;
    private final Symbol lookaheadSymbol;

    public Item(GrammarRule grammarRule, int pos, Symbol lookaheadSymbol) {
        this.grammarRule = grammarRule;
        this.pos = pos;
        this.lookaheadSymbol = lookaheadSymbol;
    }

    public GrammarRule getGrammarRule() {
        return grammarRule;
    }

    public int getPos() {
        return pos;
    }

    public boolean isPosAtEnd() {
        return this.grammarRule.symbols().size() <= pos;
    }

    public Symbol getSymbolAtPos() {
        if(!this.isPosAtEnd()) {
            return this.grammarRule.symbols().get(pos);
        }
        return null;
    }

    public Item getNextItem() {
        if(!this.isPosAtEnd()) {
            return new Item(grammarRule, pos+1, lookaheadSymbol);
        }
        return null;
    }

    public List<Symbol> getRemainingSymbols() {
        LinkedList<Symbol> remainingSymbols = new LinkedList<>();
        int size = this.grammarRule.symbols().size();
        for(int i = pos + 1; i < size; i++) {
            remainingSymbols.add(this.grammarRule.symbols().get(i));
        }
        return remainingSymbols;
    }

    public Symbol getLookaheadSymbol() {
        return lookaheadSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return pos == item.pos && Objects.equals(grammarRule, item.grammarRule) && Objects.equals(lookaheadSymbol, item.lookaheadSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grammarRule, pos, lookaheadSymbol);
    }

    @Override
    public String toString() {
        return "Item{" +
                "grammarRule=" + grammarRule +
                ", pos=" + pos +
                ", lookaheadSymbol=" + lookaheadSymbol +
                '}';
    }
}
