package parser.lr1_parser;

import grammar.Production;
import grammar.Symbol;

import java.util.*;

/**
 * Calculates the first set for a given production or a symbol
 *
 * The first set contains all possible nonterminals that can be the first occurance for the given production
 */
public class FirstSet {
    private Set<Symbol> firstSet;

    private FirstSet(Set<Symbol> firstSet) {
        // Should never be called from outside.
        this.firstSet = firstSet;
    }

    public Set<Symbol> getSymbolList() {
        return firstSet;
    }

    /**
     * Creates the first set for a given symbol list.
     * This implementation is basicly the same, as if there was a grammar production that has exactly
     * the list of symbols on the right hand side.
     *
     * E.g. createFor = [A,B,c]
     *
     * Will return FIRST(A).
     * If A is nullable, then it will return FIRST(A) concatenated with FIRST(B).
     * If B is also nullable, then it will return FIRST(A) concatenated with FIRST(B) concatenated with FIRST(c).
     */
    public static FirstSet generate(List<Production> productions, List<Symbol> createFor) {
        Production dummyProduction = new Production(null, createFor);
        return FirstSet.generate(productions, dummyProduction);
    }

    /**
     * Creates the first set for a given symbol. The symbol needs to be a nonterminal with a production
     */
    public static FirstSet generate(List<Production> productions, Symbol createFor) {
        Production dummyProduction = new Production(null, List.of(createFor));
        return FirstSet.generate(productions, dummyProduction);
    }

    /**
     * Creates the first set for a given production.
     */
    public static FirstSet generate(List<Production> productions, Production createFor) {
        HashSet<Production> history = new HashSet<>();
        history.add(createFor);
        return FirstSet.generate(productions, createFor, history);
    }

    /**
     * Creates the first set for a given production.
     * The history parameter keeps track on what productions have been called previously.
     * This is required to prevent this function to stuck in an infinite recursion.
     */
    private static FirstSet generate(List<Production> productions, Production createFor, Set<Production> history) {
        Set<Symbol> firstSet = new HashSet<>();
        Symbol first = createFor.getFirstSymbol();
        if(createFor.isNullable()) {
            firstSet.add(Symbol.EPSILON);
        } else if(first.isTerminal()) {
            firstSet.add(first);
        } else {
            boolean allNullable = true;
            for(Symbol s: createFor.getSymbols()) {
                if(s.isTerminal()) {
                    firstSet.add(s);
                    allNullable = false;
                    break;
                }
                boolean selfRecursive = false;
                List<Production> newProductions = productions.stream().filter((production) -> production.leftHandSymbol().equals(s)).toList();
                boolean nullable = false;
                for(Production production: newProductions) {
                    // Only run recursively if it is not stuck in a loop
                    if(!history.contains(production)) {
                        Set<Production> newHistory = new HashSet<>(history);
                        newHistory.add(production);
                        FirstSet tmpSet = FirstSet.generate(productions, production, newHistory);
                        if(!production.isNullable()) {
                            firstSet.addAll(tmpSet.getSymbolList());
                        } else {
                            nullable = true;
                        }
                    } else {
                        selfRecursive = true;
                    }
                }
                if(!nullable) {
                    allNullable = false;
                    break;
                }
                if(selfRecursive) {
                    break;
                }
            }
            if(allNullable) {
                // If all paths are nullable, make sure to add epsilon to the set.
                firstSet.add(Symbol.EPSILON);
            } else {
                // Since not all paths are nullable, epsilon is not a valid first symbol.
                firstSet.remove(Symbol.EPSILON);
            }
        }

        return new FirstSet(firstSet);
    }
}
