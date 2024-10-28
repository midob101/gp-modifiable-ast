package syntax_tree.ast.exceptions;

public class ReplacingUnconnectedNode extends Exception {
    public ReplacingUnconnectedNode() {
        super("Only connected nodes can be replaced in the ast. Nodes that have no parent cannot be replaced.");
    }
}
