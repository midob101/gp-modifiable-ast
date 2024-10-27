package syntax_tree;

import grammar.GrammarRule;
import grammar.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import selectors.data.ProductionSelector;
import test_selectors.TrueSelector;

import java.util.List;

public class QueryASTTest {

    @Test
    public void testFindYourselfWithQuery() {
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode("");
        QueryResult result = root.query(new TrueSelector());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(root, result.getResult().get(0));
    }

    @Test
    public void testDoNotFindYourselfWithQueryChildren() {
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode("");
        QueryResult result = root.queryChildren(new TrueSelector());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDoNotFindYourselfWithQueryImmediateChildren() {
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode("");
        QueryResult result = root.queryImmediateChildren(new TrueSelector());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testQueryImmediateChildren() {
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode("");
        AbstractSyntaxTreeNode immediateChildOne = new AbstractSyntaxTreeNode("");
        AbstractSyntaxTreeNode immediateChildTwo = new AbstractSyntaxTreeNode("");
        AbstractSyntaxTreeNode leaf = new AbstractSyntaxTreeNode("");

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        QueryResult result = root.queryImmediateChildren(new TrueSelector());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(List.of(immediateChildOne, immediateChildTwo), result.getResult());
    }

    @Test
    public void testQueryChildren() {
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode("");
        AbstractSyntaxTreeNode immediateChildOne = new AbstractSyntaxTreeNode("");
        AbstractSyntaxTreeNode immediateChildTwo = new AbstractSyntaxTreeNode("");
        AbstractSyntaxTreeNode leaf = new AbstractSyntaxTreeNode("");

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        QueryResult result = root.queryChildren(new TrueSelector());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(List.of(immediateChildOne, leaf, immediateChildTwo), result.getResult());
    }

    @Test
    public void testQueryWithFilter() {
        GrammarRule doesNotMatch = Mockito.mock(GrammarRule.class);
        Mockito.when(doesNotMatch.leftHandSymbol()).thenReturn(new Symbol("DOES_NOT_MATCH", false));
        GrammarRule doesMatch = Mockito.mock(GrammarRule.class);
        Mockito.when(doesMatch.leftHandSymbol()).thenReturn(new Symbol("MATCH", false));


        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode(doesMatch);
        AbstractSyntaxTreeNode immediateChildOne = new AbstractSyntaxTreeNode(doesMatch);
        AbstractSyntaxTreeNode immediateChildTwo = new AbstractSyntaxTreeNode(doesNotMatch);
        AbstractSyntaxTreeNode leaf = new AbstractSyntaxTreeNode(doesMatch);

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        QueryResult result = root.query(new ProductionSelector("MATCH"));
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(List.of(root, immediateChildOne, leaf), result.getResult());
    }
}
