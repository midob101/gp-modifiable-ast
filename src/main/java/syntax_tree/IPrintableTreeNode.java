package syntax_tree;

import java.util.List;

public interface IPrintableTreeNode<T extends IPrintableTreeNode> {
    String getDisplayValue();
    List<T> getChildren();
}
