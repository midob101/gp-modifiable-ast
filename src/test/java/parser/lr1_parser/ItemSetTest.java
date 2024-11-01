package parser.lr1_parser;

import grammar.Production;
import grammar.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.lr1_parser.items.Item;
import parser.lr1_parser.items.ItemSet;

import java.util.List;

public class ItemSetTest {

    @Test
    public void testHasItem() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        Production production = new Production(s1, List.of(s1, s2));

        Item item = new Item(production, 0, null);
        Item item2 = new Item(production, 0, null);

        ItemSet itemSet = new ItemSet();
        itemSet.addItem(item);


        Assertions.assertEquals(1, itemSet.getItems().size());
    }

    @Test
    public void testAddItems() {
        Symbol s1 = new Symbol("s1", false);
        Symbol s2 = new Symbol("s2", false);
        Production production = new Production(s1, List.of(s1, s2));

        Item item = new Item(production, 0, null);
        Item item2 = new Item(production, 1, null);

        ItemSet itemSet = new ItemSet();
        itemSet.addItem(item);
        itemSet.addItem(item2);

        Assertions.assertEquals(2, itemSet.getItems().size());
    }
}
