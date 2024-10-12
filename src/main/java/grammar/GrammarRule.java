package grammar;

import java.util.List;
import java.util.Objects;

/**
 * TODO: Rename to production
 * Also adjust variable names, comments, etc.
 * Keep it at production at all times.
 */
public record GrammarRule(Symbol leftHandSymbol, List<Symbol> symbols) {

    public Symbol getFirstSymbol() {
        if (this.isNullable()) {
            return null;
        }
        return symbols.get(0);
    }

    public boolean isNullable() {
        return this.symbols.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(leftHandSymbol.name()).append(" -> [");
        for (Symbol symbol : symbols) {
            output.append(symbol.name());
            if (symbol.isTerminal()) {
                output.append(" - Terminal,");
            } else {
                output.append(" - Nonterminal,");
            }
        }
        output.append("]\r\n");
        return output.toString();
    }
}
