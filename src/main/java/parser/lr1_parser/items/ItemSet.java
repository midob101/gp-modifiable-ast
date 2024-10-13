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

    /**
     * Checks if this item set already contains a similar item.
     *
     * @param item The item to check if a similar item is in this item set.
     * @return true if exists, false if no similar item exists.
     */
    public boolean hasSimilarItem(Item item) {
        for(Item existing: items) {
            if(existing.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new item to this item set.
     *
     * @param item The item added
     */
    public void addItem(Item item) {
        this.items.add(item);
    }

    /**
     * Gets all items of this item set.
     *
     * @return the item set
     */
    public Set<Item> getItems() {
        return this.items;
    }

    /**
     * Checks if this item set is empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public String toString() {
        return String.valueOf(this.internal_id);
    }
}
