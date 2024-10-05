package grammar;

import java.util.List;

public class GrammarRule {
    private Symbol leftHandSymbol = null;
    private final List<Symbol> symbols;

    public GrammarRule(Symbol leftHandSymbol, List<Symbol> symbols) {
        this.leftHandSymbol = leftHandSymbol;
        this.symbols = symbols;
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
