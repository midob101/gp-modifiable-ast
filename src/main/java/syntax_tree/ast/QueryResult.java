package syntax_tree.ast;

import selectors.BaseSelector;

import java.util.*;
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

    public QueryResult queryChildren(BaseSelector selector) {
        List<AbstractSyntaxTreeNode> matches = new LinkedList<>();

        for(AbstractSyntaxTreeNode root: result) {
            matches.addAll(root.queryChildren(selector).getResult());
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

    public static QueryResult merge(QueryResult ...queryResults) {
        HashSet<AbstractSyntaxTreeNode> alreadyAdded = new HashSet<>();
        List<AbstractSyntaxTreeNode> nodes = new LinkedList<>();
        for(QueryResult result: queryResults) {
            for(AbstractSyntaxTreeNode node: result.getResult()) {
                if(!alreadyAdded.contains(node)) {
                    nodes.add(node);
                    alreadyAdded.add(node);
                }
            }
        }
        return new QueryResult(nodes);
    }
}
