package syntax_tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import syntax_tree.ast.AbstractSyntaxTreeNode;
import syntax_tree.ast.QueryResult;
import test_selectors.TrueSelector;

import java.util.List;

public class QueryResultTest {
    @Test
    public void testQueryWithoutNodes() {
        TrueSelector selector = new TrueSelector();
        QueryResult initial = new QueryResult(List.of());
        QueryResult result = initial.query(selector);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testQueryChildrenWithoutNodes() {
        TrueSelector selector = new TrueSelector();
        QueryResult initial = new QueryResult(List.of());
        QueryResult result = initial.queryChildren(selector);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testQueryImmediateChildrenWithoutNodes() {
        TrueSelector selector = new TrueSelector();
        QueryResult initial = new QueryResult(List.of());
        QueryResult result = initial.queryImmediateChildren(selector);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testQuery() {
        TrueSelector selector = new TrueSelector();
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node.query(selector)).thenReturn(new QueryResult(List.of()));

        QueryResult initial = new QueryResult(List.of(node));
        initial.query(selector);

        Mockito.verify(node, Mockito.times(1)).query(selector);
    }
    @Test
    public void testQueryWithMultipleNodes() {
        TrueSelector selector = new TrueSelector();
        AbstractSyntaxTreeNode node1 = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node1.query(selector)).thenReturn(new QueryResult(List.of()));
        AbstractSyntaxTreeNode node2 = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node2.query(selector)).thenReturn(new QueryResult(List.of()));

        QueryResult initial = new QueryResult(List.of(node1, node2));
        initial.query(selector);

        Mockito.verify(node1, Mockito.times(1)).query(selector);
        Mockito.verify(node2, Mockito.times(1)).query(selector);
    }

    @Test
    public void testQueryChildren() {
        TrueSelector selector = new TrueSelector();
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node.queryChildren(selector)).thenReturn(new QueryResult(List.of()));

        QueryResult initial = new QueryResult(List.of(node));
        initial.queryChildren(selector);

        Mockito.verify(node, Mockito.times(1)).queryChildren(selector);
    }

    @Test
    public void testQueryChildrenWithMultipleNodes() {
        TrueSelector selector = new TrueSelector();
        AbstractSyntaxTreeNode node1 = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node1.queryChildren(selector)).thenReturn(new QueryResult(List.of()));
        AbstractSyntaxTreeNode node2 = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node2.queryChildren(selector)).thenReturn(new QueryResult(List.of()));

        QueryResult initial = new QueryResult(List.of(node1, node2));
        initial.queryChildren(selector);

        Mockito.verify(node1, Mockito.times(1)).queryChildren(selector);
        Mockito.verify(node2, Mockito.times(1)).queryChildren(selector);
    }

    @Test
    public void testQueryImmediateChildren() {
        TrueSelector selector = new TrueSelector();
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node.queryImmediateChildren(selector)).thenReturn(new QueryResult(List.of()));

        QueryResult initial = new QueryResult(List.of(node));
        initial.queryImmediateChildren(selector);

        Mockito.verify(node, Mockito.times(1)).queryImmediateChildren(selector);
    }

    @Test
    public void testQueryImmediateChildrenWithMultipleNodes() {
        TrueSelector selector = new TrueSelector();
        AbstractSyntaxTreeNode node1 = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node1.queryImmediateChildren(selector)).thenReturn(new QueryResult(List.of()));
        AbstractSyntaxTreeNode node2 = Mockito.mock(AbstractSyntaxTreeNode.class);
        Mockito.when(node2.queryImmediateChildren(selector)).thenReturn(new QueryResult(List.of()));

        QueryResult initial = new QueryResult(List.of(node1, node2));
        initial.queryImmediateChildren(selector);

        Mockito.verify(node1, Mockito.times(1)).queryImmediateChildren(selector);
        Mockito.verify(node2, Mockito.times(1)).queryImmediateChildren(selector);
    }

    @Test
    public void testMerge() {
        AbstractSyntaxTreeNode node1 = Mockito.mock(AbstractSyntaxTreeNode.class);
        AbstractSyntaxTreeNode node2 = Mockito.mock(AbstractSyntaxTreeNode.class);
        QueryResult q1 = new QueryResult(List.of(node1));
        QueryResult q2 = new QueryResult(List.of(node2));
        QueryResult result = QueryResult.merge(q1, q2);
        Assertions.assertEquals(List.of(node1, node2), result.getResult());
    }

    @Test
    public void testMergeReverseOrder() {
        AbstractSyntaxTreeNode node1 = Mockito.mock(AbstractSyntaxTreeNode.class);
        AbstractSyntaxTreeNode node2 = Mockito.mock(AbstractSyntaxTreeNode.class);
        QueryResult q1 = new QueryResult(List.of(node1));
        QueryResult q2 = new QueryResult(List.of(node2));
        QueryResult result = QueryResult.merge(q2, q1);
        Assertions.assertEquals(List.of(node2, node1), result.getResult());
    }

    @Disabled
    @Test
    public void testMergeWithOverlapsOrder() {
        AbstractSyntaxTreeNode node1 = Mockito.mock(AbstractSyntaxTreeNode.class);
        QueryResult q1 = new QueryResult(List.of(node1));
        QueryResult q2 = new QueryResult(List.of(node1));
        QueryResult result = QueryResult.merge(q2, q1);
        Assertions.assertEquals(List.of(node1), result.getResult());
    }

    @Disabled
    @Test
    public void testMergeWithOverlapsOrderMaintainingOrder() {
        AbstractSyntaxTreeNode node1 = Mockito.mock(AbstractSyntaxTreeNode.class);
        AbstractSyntaxTreeNode node2 = Mockito.mock(AbstractSyntaxTreeNode.class);
        AbstractSyntaxTreeNode node3 = Mockito.mock(AbstractSyntaxTreeNode.class);
        AbstractSyntaxTreeNode node4 = Mockito.mock(AbstractSyntaxTreeNode.class);
        QueryResult q1 = new QueryResult(List.of(node1, node2, node3));
        QueryResult q2 = new QueryResult(List.of(node2, node4));
        QueryResult result = QueryResult.merge(q2, q1);
        Assertions.assertEquals(List.of(node1, node2, node3, node4), result.getResult());
    }
}
