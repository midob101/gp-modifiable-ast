package grammar;

import java.util.List;

/**
 * TODO: Rename to production
 * Also adjust variable names, comments, etc.
 * Keep it at production at all times.
 */
public class GrammarRule {
    private Symbol leftHandSymbol = null;
    private final List<Symbol> symbols;

    public GrammarRule(Symbol leftHandSymbol, List<Symbol> symbols) {
        this.leftHandSymbol = leftHandSymbol;
        this.symbols = symbols;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public Symbol getFirstSymbol() {
        return symbols.get(0);
    }

    public Symbol getLeftHandSymbol() {
        return leftHandSymbol;
    }

    public boolean isNullable() {
        return this.symbols.size() == 1 && this.symbols.get(0) == Symbol.EPSILON;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(leftHandSymbol.name()).append(" -> [");
        for (Symbol symbol: symbols) {
            output.append(symbol.name());
            if(symbol.isTerminal()) {
                output.append(" - Terminal,");
            } else {
                output.append(" - Nonterminal,");
            }
        }
        output.append("]\r\n");
        return output.toString();
    }
}
