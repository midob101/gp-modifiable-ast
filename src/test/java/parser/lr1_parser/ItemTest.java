package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;
import org.junit.Assert;
import org.junit.Test;
import parser.lr1_parser.items.Item;

import java.util.LinkedList;
import java.util.List;

public class ItemTest {

    @Test
    public void testGetSymbolAtPosValid() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2)), 0, null);
        Assert.assertEquals(s1, item.getSymbolAtPos());

        item = new Item(new GrammarRule(s1, List.of(s1, s2)), 1, null);
        Assert.assertEquals(s2, item.getSymbolAtPos());
    }

    @Test
    public void testGetSymbolAtPosEmpty() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2)), 2, null);
        Assert.assertNull(item.getSymbolAtPos());
    }

    @Test
    public void testGetRemainingSymbols() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        Symbol s3 = new Symbol("s3", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2, s3)), 0, null);
        Assert.assertEquals(List.of(s2, s3), item.getRemainingSymbols());
    }

    @Test
    public void testGetRemainingSymbolsEmpty() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        Symbol s3 = new Symbol("s3", false);

        Item item = new Item(new GrammarRule(s1, List.of(s1, s2, s3)), 2, null);
        Assert.assertEquals(List.of(), item.getRemainingSymbols());
    }
}
