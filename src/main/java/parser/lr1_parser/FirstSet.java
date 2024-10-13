package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;

import java.util.*;

/**
 * Calculates the first set for a given grammar rule or a symbol
 *
 * The first set contains all possible nonterminals that can be the first occurance for the given grammar rule.
 */
public class FirstSet {
    private Set<Symbol> firstSet;
    // A cache for faster lookups.
    // It gets filled during the calculations

    // TODO: They should be dependent on the language definition.
    private static HashMap<GrammarRule, FirstSet> cache = new HashMap<>();

    private FirstSet(Set<Symbol> firstSet) {
        // Should never be called from outside.
        this.firstSet = firstSet;
    }

    public Set<Symbol> getSymbolList() {
        return firstSet;
    }

    public static void clearCache() {
        FirstSet.cache.clear();
    }

    /**
     * Creates the first set for a given symbol list.
     * This implementation is basicly the same, as if there was a grammar rule that has exactly
     * the list of symbols on the right hand side.
     *
     * E.g. createFor = [A,B,c]
     *
     * Will return FIRST(A).
     * If A is nullable, then it will return FIRST(A) concatenated with FIRST(B).
     * If B is also nullable, then it will return FIRST(A) concatenated with FIRST(B) concatenated with FIRST(c).
     */
    public static FirstSet generate(List<GrammarRule> grammarRules, List<Symbol> createFor) {
        GrammarRule dummyRule = new GrammarRule(null, createFor);
        return FirstSet.generate(grammarRules, dummyRule);
    }

    /**
     * Creates the first set for a given symbol. The symbol needs to be a nonterminal with a production
     */
    public static FirstSet generate(List<GrammarRule> grammarRules, Symbol createFor) {
        GrammarRule dummyRule = new GrammarRule(null, List.of(createFor));
        return FirstSet.generate(grammarRules, dummyRule);
    }

    /**
     * Creates the first set for a given production.
     */
    public static FirstSet generate(List<GrammarRule> grammarRules, GrammarRule createFor) {
        HashSet<GrammarRule> history = new HashSet<>();
        history.add(createFor);
        return FirstSet.generate(grammarRules, createFor, history);
    }

    /**
     * Creates the first set for a given production.
     * The history parameter keeps track on what rules have been called previously.
     * This is required to prevent this function to stuck in an infinite recursion.
     */
    private static FirstSet generate(List<GrammarRule> grammarRules, GrammarRule createFor, Set<GrammarRule> history) {
        if(FirstSet.cache.containsKey(createFor)) {
            return FirstSet.cache.get(createFor);
        }

        Set<Symbol> firstSet = new HashSet<>();
        Symbol first = createFor.getFirstSymbol();
        if(createFor.isNullable()) {
            firstSet.add(Symbol.EPSILON);
        } else if(first.isTerminal()) {
            firstSet.add(first);
        } else {
            boolean allNullable = true;
            for(Symbol s: createFor.symbols()) {
                if(s.isTerminal()) {
                    firstSet.add(s);
                    allNullable = false;
                    break;
                }
                boolean selfRecursive = false;
                List<GrammarRule> newRules = grammarRules.stream().filter((rule) -> rule.leftHandSymbol().equals(s)).toList();
                boolean nullable = false;
                for(GrammarRule rule: newRules) {
                    // Only run recursively if it is not stuck in a loop
                    if(!history.contains(rule)) {
                        Set<GrammarRule> newHistory = new HashSet<>(history);
                        newHistory.add(rule);
                        FirstSet tmpSet = FirstSet.generate(grammarRules, rule, newHistory);
                        if(!rule.isNullable()) {
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

        FirstSet.cache.put(createFor, new FirstSet(firstSet));
        return FirstSet.cache.get(createFor);
    }
}
