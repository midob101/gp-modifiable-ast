package parser.lr1_parser.items;

import grammar.Production;
import grammar.Symbol;

import java.util.LinkedList;
import java.util.List;

public record Item(Production production, int pos, Symbol lookaheadSymbol) {

    /**
     * @return true if the position is at the end of the production
     */
    public boolean isPosAtEnd() {
        return this.production.getSymbols().size() <= pos;
    }

    /**
     * @return the symbol in the production that is at the current position
     */
    public Symbol getSymbolAtPos() {
        if (!this.isPosAtEnd()) {
            return this.production.getSymbols().get(pos);
        }
        return null;
    }

    /**
     * @return a new item, with the same production and lookahead but the position advanced to the next symbol.
     */
    public Item getNextItem() {
        if (!this.isPosAtEnd()) {
            return new Item(production, pos + 1, lookaheadSymbol);
        }
        return null;
    }

    /**
     * @return a list of the symbols that are after the current symbol marked by position.
     */
    public List<Symbol> getRemainingSymbols() {
        LinkedList<Symbol> remainingSymbols = new LinkedList<>();
        int size = this.production.getSymbols().size();
        for (int i = pos + 1; i < size; i++) {
            remainingSymbols.add(this.production.getSymbols().get(i));
        }
        return remainingSymbols;
    }

    @Override
    public String toString() {
        return "Item{" +
                "production=" + production +
                ", pos=" + pos +
                ", lookaheadSymbol=" + lookaheadSymbol +
                '}';
    }
}
