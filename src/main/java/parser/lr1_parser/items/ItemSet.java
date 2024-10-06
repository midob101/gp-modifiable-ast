package parser.lr1_parser.items;

import java.util.LinkedHashSet;
import java.util.Set;

public class ItemSet {

    private static int id = 0;
    private int internal_id;

    // Use a LinkedHashSet to guarantee the same order every time when printing the items
    // This is only required for the test suites to reliably detect where which entry is.
    private Set<Item> items = new LinkedHashSet<>();

    public ItemSet() {
        this.internal_id = ItemSet.id;
        ItemSet.id = ItemSet.id + 1;
    }

    public boolean hasSimilarItem(Item item) {
        for(Item existing: items) {
            if(existing.isEqualTo(item)) {
                return true;
            }
        }
        return false;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public String toString() {
        return String.valueOf(this.internal_id);
    }
}
