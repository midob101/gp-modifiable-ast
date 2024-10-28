package syntax_tree.ast.exceptions;

public class AddingConnectedNode extends Exception {
    public AddingConnectedNode() {
        super("Only dangling nodes can be added to the ast. Nodes that have a parent cannot be added.");
    }
}
