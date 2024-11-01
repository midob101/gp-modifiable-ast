package grammar;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class Production {
    private final Symbol leftHandSymbol;
    private final List<Symbol> symbols;
    private final List<List<SymbolModifier>> symbolModifiers;
    private final List<SymbolModifier> leftHandModifiers;

    /**
     *
     */
    public Production(Symbol leftHandSymbol, List<Symbol> symbols, List<List<SymbolModifier>> symbolModifiers, List<SymbolModifier> leftHandModifiers) {
        if(symbolModifiers.size() != symbols.size()) {
            throw new RuntimeException("The symbol modifiers list has to be same length as the symbols list");
        }
        this.leftHandSymbol = leftHandSymbol;
        this.symbols = symbols;
        this.symbolModifiers = symbolModifiers;
        this.leftHandModifiers = leftHandModifiers;
    }

    public Production(Symbol leftHandSymbol, List<Symbol> symbols) {
        this.leftHandSymbol = leftHandSymbol;
        this.symbols = symbols;
        this.symbolModifiers = new LinkedList<>();
        for(Symbol s: symbols) {
            symbolModifiers.add(new LinkedList<>());
        }
        this.leftHandModifiers = new LinkedList<>();
    }

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

    public Symbol leftHandSymbol() {
        return leftHandSymbol;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public List<List<SymbolModifier>> getSymbolModifiers() {
        return symbolModifiers;
    }

    public List<SymbolModifier> getLeftHandModifiers() {
        return leftHandModifiers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Production) obj;
        return Objects.equals(this.leftHandSymbol, that.leftHandSymbol) &&
                Objects.equals(this.symbols, that.symbols) &&
                Objects.equals(this.symbolModifiers, that.symbolModifiers) &&
                Objects.equals(this.leftHandModifiers, that.leftHandModifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftHandSymbol, symbols, symbolModifiers, leftHandModifiers);
    }

}
