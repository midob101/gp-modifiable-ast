package syntax_tree;

import grammar.GrammarRule;
import lexer.Token;

import java.util.LinkedList;
import java.util.List;

public class AbstractSyntaxTreeNode implements IPrintableTreeNode<AbstractSyntaxTreeNode> {
    protected Token token = null;
    protected GrammarRule rule = null;
    protected LinkedList<AbstractSyntaxTreeNode> children = new LinkedList<>();
    private boolean hidden = false;
    private String alias = null;

    public AbstractSyntaxTreeNode(Token token) {
        this.token = token;
    }

    public AbstractSyntaxTreeNode(GrammarRule rule) {
        this.rule = rule;
    }

    public Token getToken() {
        return token;
    }

    public GrammarRule getRule() {
        return rule;
    }

    public void addChild(AbstractSyntaxTreeNode child) {
        children.add(child);
    }

    protected void setHidden() {
        this.hidden = true;
    }

    @Override
    public String getDisplayValue() {
        if(this.rule != null) {
            return this.alias != null ? this.alias : getRule().leftHandSymbol().name();
        } else if(getToken() != null) {
            return (this.alias != null ? this.alias : getToken().getLexerDefinition().getName()) + ": " + getToken().getValue();
        }
        return "UNDEFINED";
    }

    private boolean isVisible() {
        return !this.hidden;
    }

    @Override
    public List<AbstractSyntaxTreeNode> getChildren() {
        return children.stream().filter(AbstractSyntaxTreeNode::isVisible).toList();
    }

    public void setAlias(String value) {
        this.alias = value;
    }
}
