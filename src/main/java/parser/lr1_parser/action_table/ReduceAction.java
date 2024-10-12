package parser.lr1_parser.action_table;

import grammar.GrammarRule;

import java.util.Objects;

public class ReduceAction extends BaseAction {
    GrammarRule reducedRule;

    public ReduceAction(GrammarRule reducedRule) {
        this.reducedRule = reducedRule;
    }

    public GrammarRule getReducedRule() {
        return reducedRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReduceAction that)) return false;
        return Objects.equals(reducedRule, that.reducedRule);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(reducedRule);
    }

    @Override
    public String toString() {
        return "ReduceAction{GrammarProduction=" + reducedRule + "}";
    }
}
