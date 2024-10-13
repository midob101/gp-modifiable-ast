package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;

import java.util.LinkedList;
import java.util.List;

public record Item(GrammarRule grammarRule, int pos, Symbol lookaheadSymbol) {

    /**
     * @return true if the position is at the end of the grammar rule
     */
    public boolean isPosAtEnd() {
        return this.grammarRule.symbols().size() <= pos;
    }

    /**
     * @return the symbol in the grammar rule that is at the current position
     */
    public Symbol getSymbolAtPos() {
        if (!this.isPosAtEnd()) {
            return this.grammarRule.symbols().get(pos);
        }
        return null;
    }

    /**
     * @return a new item, with the same grammar rule and lookahead but the position advanced to the next symbol.
     */
    public Item getNextItem() {
        if (!this.isPosAtEnd()) {
            return new Item(grammarRule, pos + 1, lookaheadSymbol);
        }
        return null;
    }

    /**
     * @return a list of the symbols that are after the current symbol marked by position.
     */
    public List<Symbol> getRemainingSymbols() {
        LinkedList<Symbol> remainingSymbols = new LinkedList<>();
        int size = this.grammarRule.symbols().size();
        for (int i = pos + 1; i < size; i++) {
            remainingSymbols.add(this.grammarRule.symbols().get(i));
        }
        return remainingSymbols;
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
