package concrete_syntax_tree;

import grammar.GrammarRule;
import grammar.Symbol;
import lexer.Token;

import java.util.Iterator;
import java.util.LinkedList;

public class ConcreteSyntaxTreeNode {
    private Token token = null;
    private Symbol symbol = null;
    private GrammarRule rule = null;
    private LinkedList<ConcreteSyntaxTreeNode> children = new LinkedList<>();

    public ConcreteSyntaxTreeNode(Token token, Symbol symbol) {
        this.token = token;
        this.symbol = symbol;
    }

    public ConcreteSyntaxTreeNode(GrammarRule rule) {
        this.rule = rule;
    }

    public Token getToken() {
        return token;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public GrammarRule getRule() {
        return rule;
    }

    public LinkedList<ConcreteSyntaxTreeNode> getChildren() {
        return children;
    }

    public void addChild(ConcreteSyntaxTreeNode child) {
        children.push(child);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        print(buffer, "", "");
        return buffer.toString();
    }

    /**
     * Implementation from https://stackoverflow.com/a/8948691
     *
     * @param buffer
     * @param prefix
     * @param childrenPrefix
     */
    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        if(this.rule != null) {
            buffer.append(getRule().leftHandSymbol().name());
        } else if(getToken() != null) {
            if(getToken().getKeepInAST()) {
                buffer.append(getToken().getValue());
            } else {
                buffer.append(getToken().getLexerDefinition().getName());
            }
        } else {
            buffer.append("UNDEFINED");
        }
        buffer.append('\n');
        for (Iterator<ConcreteSyntaxTreeNode> it = children.iterator(); it.hasNext();) {
            ConcreteSyntaxTreeNode next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
