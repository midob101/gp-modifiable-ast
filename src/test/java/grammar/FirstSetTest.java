package grammar;

import org.junit.Assert;
import org.junit.Test;

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
        GrammarRule g3 = new GrammarRule(A, List.of(Symbol.EPSILON));
        GrammarRule g4 = new GrammarRule(B, List.of(B, a, A, C));
        GrammarRule g5 = new GrammarRule(B, List.of(c));
        GrammarRule g6 = new GrammarRule(C, List.of(b));
        GrammarRule g7 = new GrammarRule(C, List.of(Symbol.EPSILON));

        List<GrammarRule> grammar = List.of(g1, g2, g3, g4, g5, g6, g7);

        Set<Symbol> symbolSet = FirstSet.generate(grammar, S).getSymbolList();
        Assert.assertEquals(Set.of(a, b, c), symbolSet);

        symbolSet = FirstSet.generate(grammar, A).getSymbolList();
        Assert.assertEquals(Set.of(a, b, Symbol.EPSILON), symbolSet);

        symbolSet = FirstSet.generate(grammar, B).getSymbolList();
        Assert.assertEquals(Set.of(c), symbolSet);

        symbolSet = FirstSet.generate(grammar, C).getSymbolList();
        Assert.assertEquals(Set.of(b, Symbol.EPSILON), symbolSet);
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
        Assert.assertEquals(Set.of(), symbolSet);
    }
}
