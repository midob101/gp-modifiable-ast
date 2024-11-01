package syntax_tree;

import grammar.Production;
import lexer.Token;

import java.util.LinkedList;

public class ConcreteSyntaxTreeNode implements IPrintableTreeNode<ConcreteSyntaxTreeNode> {
    protected Token token = null;
    protected Production production = null;
    protected LinkedList<ConcreteSyntaxTreeNode> children = new LinkedList<>();

    public ConcreteSyntaxTreeNode(Token token) {
        this.token = token;
    }

    public ConcreteSyntaxTreeNode(Production production) {
        this.production = production;
    }

    public Token getToken() {
        return token;
    }

    public Production getProduction() {
        return production;
    }

    public void addChild(ConcreteSyntaxTreeNode child) {
        children.addFirst(child);
    }

    @Override
    public String getDisplayValue() {
        if(this.production != null) {
            return getProduction().leftHandSymbol().name();
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
