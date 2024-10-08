package parser.lr1_parser.action_table;

import parser.lr1_parser.items.ItemSet;

import java.util.Objects;

public class ShiftAction extends BaseAction {
    private final ItemSet shiftTo;

    public ShiftAction(ItemSet shiftTo) {
        this.shiftTo = shiftTo;
    }

    public ItemSet getShiftTo() {
        return shiftTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftAction that)) return false;
        return Objects.equals(shiftTo, that.shiftTo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shiftTo);
    }
}
