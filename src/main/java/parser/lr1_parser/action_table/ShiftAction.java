package parser.lr1_parser.action_table;

import parser.lr1_parser.items.ItemSet;

public class ShiftAction extends BaseAction {
    private final ItemSet shiftTo;

    public ShiftAction(ItemSet shiftTo) {
        this.shiftTo = shiftTo;
    }

    public ItemSet getShiftTo() {
        return shiftTo;
    }
}
