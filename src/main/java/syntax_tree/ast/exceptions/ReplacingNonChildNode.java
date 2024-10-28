package syntax_tree.ast.exceptions;

public class ReplacingNonChildNode extends Exception {
    public ReplacingNonChildNode() {
        super("Only direct childrens can be replaced in the ast. Nodes have a different parent cannot be replaced.");
    }
}
