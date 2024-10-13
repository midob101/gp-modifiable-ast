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
    private AbstractSyntaxTreeNode parent = null;

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

    protected void setParent(AbstractSyntaxTreeNode parent) {
        this.parent = parent;
    }

    public AbstractSyntaxTreeNode getParent() {
        return parent;
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

    public List<AbstractSyntaxTreeNode> getAllChildren() {
        return children;
    }

    public void setAlias(String value) {
        this.alias = value;
    }

    public void replaceChild(AbstractSyntaxTreeNode original, List<AbstractSyntaxTreeNode> newChilds) {
        int idx = children.indexOf(original);
        children.addAll(idx, newChilds);
        children.remove(original);
    }

    public String getSourceCode() {
        StringBuilder sb = new StringBuilder();
        createSourceCode(sb);
        return sb.toString();
    }

    private void createSourceCode(StringBuilder builder) {
        if(this.token != null) {
            builder.append(token.getValue());
        }
        for(AbstractSyntaxTreeNode treeNode: getAllChildren()) {
            treeNode.createSourceCode(builder);
        }
    }
}
