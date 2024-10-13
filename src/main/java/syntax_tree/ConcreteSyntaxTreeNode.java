package syntax_tree;

import grammar.GrammarRule;
import lexer.Token;

import java.util.LinkedList;

public class ConcreteSyntaxTreeNode implements IPrintableTreeNode<ConcreteSyntaxTreeNode> {
    protected Token token = null;
    protected GrammarRule rule = null;
    protected LinkedList<ConcreteSyntaxTreeNode> children = new LinkedList<>();

    public ConcreteSyntaxTreeNode(Token token) {
        this.token = token;
    }

    public ConcreteSyntaxTreeNode(GrammarRule rule) {
        this.rule = rule;
    }

    public Token getToken() {
        return token;
    }

    public GrammarRule getRule() {
        return rule;
    }

    public void addChild(ConcreteSyntaxTreeNode child) {
        children.addFirst(child);
    }

    @Override
    public String getDisplayValue() {
        if(this.rule != null) {
            return getRule().leftHandSymbol().name();
        } else if(getToken() != null) {
            if(getToken().getKeepInAST()) {
                return getToken().getValue();
            } else {
                return getToken().getLexerDefinition().getName();
            }
        }
        return "UNDEFINED";
    }

    public LinkedList<ConcreteSyntaxTreeNode> getChildren() {
        return children;
    }
}
