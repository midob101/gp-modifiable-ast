package parser.lr1_parser.items;

import grammar.GrammarRule;
import grammar.Symbol;

public class PrettyPrintItemFamily {

    /**
     * Utility function to pretty print an entire item family.
     * Returns the output as a string.
     */
    public static String getItemFamilyString(ItemFamily itemFamily) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(ItemSet itemSet: itemFamily.getItemSets()) {
            sb.append("I_").append(itemSet).append(":     ");
            for(Item item: itemSet.getItems()) {
                GrammarRule rule = item.getGrammarRule();
                sb.append(rule.leftHandSymbol().name())
                        .append(" -> ");
                int curPos = 0;
                for(Symbol symbol: rule.symbols()) {
                    if(curPos == item.getPos()) {
                        sb.append(".");
                    }
                    sb.append(symbol.name()).append(" ");
                    curPos++;
                }
                if(curPos == item.getPos()) {
                    sb.append(".");
                }
                sb.append(", ").append(item.getLookaheadSymbol().name());
                sb.append("\n         ");
            }
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }
}
