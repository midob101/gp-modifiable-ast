package grammar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.lr1_parser.FirstSet;

import java.util.List;
import java.util.Set;

public class FirstSetTest {

    @Test
    public void testFirstSet() {
        /**
         * Test for the first set of the following grammar:
         *
         * S    ->      AB
         * A    ->      Ca | epsilon
         * B    ->      BaAC | c
         * C    ->      b | epsilon
         *
         * Example from https://web.stanford.edu/class/archive/cs/cs143/cs143.1128/handouts/090%20Top-Down%20Parsing.pdf
         */
        Symbol S = new Symbol("S", false);
        Symbol A = new Symbol("A", false);
        Symbol B = new Symbol("B", false);
        Symbol C = new Symbol("C", false);
        Symbol a = new Symbol("a", true);
        Symbol b = new Symbol("b", true);
        Symbol c = new Symbol("c", true);

        GrammarRule g1 = new GrammarRule(S, List.of(A, B));
        GrammarRule g2 = new GrammarRule(A, List.of(C, a));
        GrammarRule g3 = new GrammarRule(A, List.of());
        GrammarRule g4 = new GrammarRule(B, List.of(B, a, A, C));
        GrammarRule g5 = new GrammarRule(B, List.of(c));
        GrammarRule g6 = new GrammarRule(C, List.of(b));
        GrammarRule g7 = new GrammarRule(C, List.of());

        List<GrammarRule> grammar = List.of(g1, g2, g3, g4, g5, g6, g7);

        Set<Symbol> symbolSet = FirstSet.generate(grammar, S).getSymbolList();
        Assertions.assertEquals(Set.of(a, b, c), symbolSet);

        symbolSet = FirstSet.generate(grammar, A).getSymbolList();
        Assertions.assertEquals(Set.of(a, b, Symbol.EPSILON), symbolSet);

        symbolSet = FirstSet.generate(grammar, B).getSymbolList();
        Assertions.assertEquals(Set.of(c), symbolSet);

        symbolSet = FirstSet.generate(grammar, C).getSymbolList();
        Assertions.assertEquals(Set.of(b, Symbol.EPSILON), symbolSet);
    }

    @Test
    public void testFirstSetIndirectRecursive() {
        /**
         * Test for the first set of the following grammar:
         *
         * A    ->   B
         * B    ->   A
         *
         * The first set calculation should not be stuck in an infinite loop and instead return an empty set.
         */
        Symbol A = new Symbol("A", false);
        Symbol B = new Symbol("B", false);

        GrammarRule g1 = new GrammarRule(A, List.of(B));
        GrammarRule g2 = new GrammarRule(B, List.of(A));

        List<GrammarRule> grammar = List.of(g1, g2);

        Set<Symbol> symbolSet = FirstSet.generate(grammar, A).getSymbolList();
        Assertions.assertEquals(Set.of(), symbolSet);
    }

    @Test
    public void testFirstSetForSetOfSymbols() {
        /**
         * Test for the first set of the following grammar:
         *
         * A    ->   B | epsilon
         * B    ->   C | epsilon
         * C    ->   c
         */
        Symbol A = new Symbol("A", false);
        Symbol B = new Symbol("B", false);
        Symbol C = new Symbol("C", false);
        Symbol c = new Symbol("c", true);

        GrammarRule g1 = new GrammarRule(A, List.of(B));
        GrammarRule g2 = new GrammarRule(A, List.of());
        GrammarRule g3 = new GrammarRule(B, List.of(C));
        GrammarRule g4 = new GrammarRule(B, List.of());
        GrammarRule g5 = new GrammarRule(C, List.of(c));

        List<GrammarRule> grammar = List.of(g1, g2, g3, g4, g5);

        /**
         * This should return the first symbol set of a grammar rule equivalent to
         *      DUMMY   ->  ABc
         * That should be only c.
         */
        Set<Symbol> symbolSet = FirstSet.generate(grammar, List.of(A,B,c)).getSymbolList();
        Assertions.assertEquals(Set.of(c), symbolSet);

        /**
         * This should return the first symbol set of a grammar rule equivalent to
         *      DUMMY   ->  AB
         * That should be c and epsilon, as the c is not required anymore.
         */
        symbolSet = FirstSet.generate(grammar, List.of(A,B)).getSymbolList();
        Assertions.assertEquals(Set.of(c, Symbol.EPSILON), symbolSet);

        /**
         * This should return the first symbol of the grammar rule with A on the left hand side.
         * This is c and epsilon
         *
         * The return values are different, because the method above requires the c at the end.
         */
        symbolSet = FirstSet.generate(grammar, A).getSymbolList();
        Assertions.assertEquals(Set.of(c, Symbol.EPSILON), symbolSet);
    }
}
