package parser.lr1_parser;

import grammar.Production;
import grammar.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.lr1_parser.items.Item;
import parser.lr1_parser.items.ItemSet;
import test_utils.StringUtilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ClosureTest {

    @Test
    public void testClosure() throws IOException {
        /**
         * Grammar:
         *      S   ->      AB
         *      A   ->      a | epsilon
         *      B   ->      b
         *
         * Item:
         *      S   ->  .AB, $
         * FIRST(B$) = {b}
         *
         * Resulting item set:
         *      S   ->  .AB, $
         *      A   ->  .a, b
         *      A   ->  .epsilon, b
         */

        Symbol S = new Symbol("S", false);
        Symbol A = new Symbol("A", false);
        Symbol B = new Symbol("B", false);
        Symbol a = new Symbol("a", true);
        Symbol b = new Symbol("b", true);

        Production g1 = new Production(S, List.of(A, B));
        Production g2 = new Production(A, List.of(a));
        Production g3 = new Production(A, List.of(Symbol.EPSILON));
        Production g4 = new Production(B, List.of(b));

        Item start = new Item(g1, 0, Symbol.END_OF_INPUT);
        ItemSet itemSet = new ItemSet();
        itemSet.addItem(start);

        Closure.decorateClosure(List.of(g1,g2,g3,g4), itemSet);

        String expected = Files.readString(Path.of("src/test/java/parser/lr1_parser/test_data/ClosureTest.txt"), StandardCharsets.UTF_8);
        Assertions.assertEquals(StringUtilities.useCRLF(expected), StringUtilities.useCRLF(itemSet.getItems().toString()));
    }

    @Test
    public void testClosure2() throws IOException {
        /**
         * Grammar:
         *      S   ->      AB
         *      A   ->      a | epsilon
         *      B   ->      b
         *
         * Item:
         *      S   ->  A.B, a
         * FIRST(a) = {a}
         *
         * Resulting item set:
         *      S   ->  A.B, a
         *      B   ->  .b, a
         */

        Symbol S = new Symbol("S", false);
        Symbol A = new Symbol("A", false);
        Symbol B = new Symbol("B", false);
        Symbol a = new Symbol("a", true);
        Symbol b = new Symbol("b", true);

        Production g1 = new Production(S, List.of(A, B));
        Production g2 = new Production(A, List.of(a));
        Production g3 = new Production(A, List.of(Symbol.EPSILON));
        Production g4 = new Production(B, List.of(b));

        Item start = new Item(g1, 1, a);
        ItemSet itemSet = new ItemSet();
        itemSet.addItem(start);

        Closure.decorateClosure(List.of(g1,g2,g3,g4), itemSet);

        String expected = Files.readString(Path.of("src/test/java/parser/lr1_parser/test_data/ClosureTest2.txt"), StandardCharsets.UTF_8);
        Assertions.assertEquals(StringUtilities.useCRLF(expected), StringUtilities.useCRLF(itemSet.getItems().toString()));
    }
}
