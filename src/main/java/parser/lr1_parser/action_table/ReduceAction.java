package parser.lr1_parser.action_table;

import grammar.Production;

import java.util.Objects;

public class ReduceAction extends BaseAction {
    Production reducedProduction;

    public ReduceAction(Production reducedProduction) {
        this.reducedProduction = reducedProduction;
    }

    public Production getReducedProduction() {
        return reducedProduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReduceAction that)) return false;
        return Objects.equals(reducedProduction, that.reducedProduction);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(reducedProduction);
    }

    @Override
    public String toString() {
        return "ReduceAction{GrammarProduction=" + reducedProduction + "}";
    }
}
