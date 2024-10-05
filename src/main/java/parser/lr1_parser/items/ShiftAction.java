package parser.lr1_parser.items;

public class ShiftAction extends BaseAction {
    private final ItemSet shiftTo;

    public ShiftAction(ItemSet shiftTo) {
        this.shiftTo = shiftTo;
    }

    public ItemSet getShiftTo() {
        return shiftTo;
    }
}
