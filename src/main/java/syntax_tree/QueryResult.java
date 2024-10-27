package syntax_tree;

import selectors.BaseSelector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class QueryResult implements Iterable<AbstractSyntaxTreeNode> {

    List<AbstractSyntaxTreeNode> result;

    public QueryResult(List<AbstractSyntaxTreeNode> result) {
        this.result = result;
    }

    public QueryResult query(BaseSelector selector) {
        List<AbstractSyntaxTreeNode> matches = new LinkedList<>();

        for(AbstractSyntaxTreeNode root: result) {
            matches.addAll(root.query(selector).getResult());
        }

        return new QueryResult(matches);
    }

    public QueryResult queryImmediateChildren(BaseSelector selector) {
        List<AbstractSyntaxTreeNode> matches = new LinkedList<>();

        for(AbstractSyntaxTreeNode root: result) {
            matches.addAll(root.queryImmediateChildren(selector).getResult());
        }

        return new QueryResult(matches);
    }

    @Override
    public Iterator<AbstractSyntaxTreeNode> iterator() {
        return this.result.iterator();
    }

    @Override
    public void forEach(Consumer<? super AbstractSyntaxTreeNode> action) {
        this.result.forEach(action);
    }

    @Override
    public Spliterator<AbstractSyntaxTreeNode> spliterator() {
        return this.result.spliterator();
    }

    public List<AbstractSyntaxTreeNode> getResult() {
        return result;
    }

    public boolean isEmpty() {
        return this.result.isEmpty();
    }
}
