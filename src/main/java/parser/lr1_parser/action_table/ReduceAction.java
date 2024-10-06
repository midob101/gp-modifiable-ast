package parser.lr1_parser.action_table;

import grammar.GrammarRule;

public class ReduceAction extends BaseAction {
    GrammarRule reducedRule;

    public ReduceAction(GrammarRule reducedRule) {
        this.reducedRule = reducedRule;
    }

    public GrammarRule getReducedRule() {
        return reducedRule;
    }
}
