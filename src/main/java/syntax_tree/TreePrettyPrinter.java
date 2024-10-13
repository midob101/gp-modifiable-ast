package syntax_tree;

import java.util.Iterator;

public class TreePrettyPrinter {

    public static String print(IPrintableTreeNode treeNode) {
        StringBuilder buffer = new StringBuilder();
        print(treeNode, buffer, "", "");
        return buffer.toString();
    }

    /**
     * Implementation from https://stackoverflow.com/a/8948691
     *
     * @param buffer
     * @param prefix
     * @param childrenPrefix
     */
    private static void print(IPrintableTreeNode treeNode, StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(treeNode.getDisplayValue());
        buffer.append('\n');
        for (Iterator<IPrintableTreeNode> it = treeNode.getChildren().iterator(); it.hasNext();) {
            IPrintableTreeNode next = it.next();
            if (it.hasNext()) {
                print(next, buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                print(next, buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
