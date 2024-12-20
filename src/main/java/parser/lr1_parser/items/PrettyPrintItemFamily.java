package parser.lr1_parser.items;

import grammar.Production;
import grammar.Symbol;

public class PrettyPrintItemFamily {

    /**
     * Utility function to pretty print an entire item family.
     * Returns the output as a string.
     *
     * @param itemFamily The item family to print
     */
    public static String getItemFamilyString(ItemFamily itemFamily) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(ItemSet itemSet: itemFamily.getItemSets()) {
            sb.append("I_").append(itemSet).append(":     ");
            for(Item item: itemSet.getItems()) {
                Production production = item.production();
                sb.append(production.leftHandSymbol().name())
                        .append(" -> ");
                int curPos = 0;
                for(Symbol symbol: production.getSymbols()) {
                    if(curPos == item.pos()) {
                        sb.append(".");
                    }
                    sb.append(symbol.name()).append(" ");
                    curPos++;
                }
                if(curPos == item.pos()) {
                    sb.append(".");
                }
                sb.append(", ").append(item.lookaheadSymbol().name());
                sb.append("\n         ");
            }
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }
}
