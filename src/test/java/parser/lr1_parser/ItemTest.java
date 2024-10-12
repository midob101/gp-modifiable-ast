package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.lr1_parser.action_table.ReduceAction;
import parser.lr1_parser.items.Item;

import java.util.LinkedList;
import java.util.List;

public class ItemTest {

    @Test
    public void testGetSymbolAtPosValid() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2)), 0, null);
        Assertions.assertEquals(s1, item.getSymbolAtPos());

        item = new Item(new GrammarRule(s1, List.of(s1, s2)), 1, null);
        Assertions.assertEquals(s2, item.getSymbolAtPos());
    }

    @Test
    public void testGetSymbolAtPosEmpty() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2)), 2, null);
        Assertions.assertNull(item.getSymbolAtPos());
    }

    @Test
    public void testGetRemainingSymbols() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        Symbol s3 = new Symbol("s3", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2, s3)), 0, null);
        Assertions.assertEquals(List.of(s2, s3), item.getRemainingSymbols());
    }

    @Test
    public void testGetRemainingSymbolsEmpty() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        Symbol s3 = new Symbol("s3", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2, s3)), 2, null);
        Assertions.assertEquals(List.of(), item.getRemainingSymbols());
    }

    @Test
    public void testGetRemainingSymbolsMiddle() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        Symbol s3 = new Symbol("s3", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2, s3)), 1, null);
        Assertions.assertEquals(List.of(s3), item.getRemainingSymbols());
    }

    @Test
    public void LinkedListTest() {
        Symbol a = new Symbol("asd", true);
        Symbol b = new Symbol("asd", true);

        LinkedList<Symbol> la = new LinkedList<>();
        la.add(a);
        LinkedList<Symbol> la2 = new LinkedList<>();
        la2.add(b);

        GrammarRule g1 = new GrammarRule(a, la);
        GrammarRule g2 = new GrammarRule(b, la2);

        ReduceAction r1 = new ReduceAction(g1);
        ReduceAction r2 = new ReduceAction(g2);

        Assertions.assertTrue(r1.equals(r2));
    }
}
