package parser.lr1_parser;

import grammar.GrammarRule;
import grammar.Symbol;
import org.junit.Assert;
import org.junit.Test;
import parser.lr1_parser.items.Item;
import parser.lr1_parser.items.ItemSet;

import java.util.List;

public class ItemSetTest {

    @Test
    public void testHasItem() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        GrammarRule grammarRule = new GrammarRule(s1, List.of(s1, s2));

        Item item = new Item(grammarRule, 0, null);
        Item item2 = new Item(grammarRule, 0, null);

        ItemSet itemSet = new ItemSet();
        itemSet.addItem(item);


        Assert.assertTrue(itemSet.hasSimilarItem(item2));
    }

    @Test
    public void testAddItems() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        GrammarRule grammarRule = new GrammarRule(s1, List.of(s1, s2));

        Item item = new Item(grammarRule, 0, null);
        Item item2 = new Item(grammarRule, 1, null);

        ItemSet itemSet = new ItemSet();
        itemSet.addItem(item);
        itemSet.addItem(item2);

        Assert.assertEquals(2, itemSet.getItems().size());
    }
}
