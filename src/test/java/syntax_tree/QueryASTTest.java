package syntax_tree;

import grammar.GrammarRule;
import grammar.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import selectors.data.ProductionSelector;
import syntax_tree.ast.ProductionTreeNode;
import syntax_tree.ast.QueryResult;
import syntax_tree.ast.StringTreeNode;
import syntax_tree.ast.exceptions.AddingConnectedNode;
import test_utils.selector_stubs.TrueSelectorStub;

import java.util.List;

public class QueryASTTest {

    @Test
    public void testFindYourselfWithQuery() {
        StringTreeNode root = new StringTreeNode("");
        QueryResult result = root.query(new TrueSelectorStub());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(root, result.getResult().get(0));
    }

    @Test
    public void testDoNotFindYourselfWithQueryChildren() {
        StringTreeNode root = new StringTreeNode("");
        QueryResult result = root.queryChildren(new TrueSelectorStub());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDoNotFindYourselfWithQueryImmediateChildren() {
        StringTreeNode root = new StringTreeNode("");
        QueryResult result = root.queryImmediateChildren(new TrueSelectorStub());
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testQueryImmediateChildren() throws AddingConnectedNode {
        StringTreeNode root = new StringTreeNode("");
        StringTreeNode immediateChildOne = new StringTreeNode("");
        StringTreeNode immediateChildTwo = new StringTreeNode("");
        StringTreeNode leaf = new StringTreeNode("");

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        QueryResult result = root.queryImmediateChildren(new TrueSelectorStub());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(List.of(immediateChildOne, immediateChildTwo), result.getResult());
    }

    @Test
    public void testQueryChildren() throws AddingConnectedNode {
        StringTreeNode root = new StringTreeNode("");
        StringTreeNode immediateChildOne = new StringTreeNode("");
        StringTreeNode immediateChildTwo = new StringTreeNode("");
        StringTreeNode leaf = new StringTreeNode("");

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        QueryResult result = root.queryChildren(new TrueSelectorStub());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(List.of(immediateChildOne, leaf, immediateChildTwo), result.getResult());
    }

    @Test
    public void testQueryWithFilter() throws AddingConnectedNode {
        GrammarRule doesNotMatch = Mockito.mock(GrammarRule.class);
        Mockito.when(doesNotMatch.leftHandSymbol()).thenReturn(new Symbol("DOES_NOT_MATCH", false));
        GrammarRule doesMatch = Mockito.mock(GrammarRule.class);
        Mockito.when(doesMatch.leftHandSymbol()).thenReturn(new Symbol("MATCH", false));


        ProductionTreeNode root = new ProductionTreeNode(doesMatch);
        ProductionTreeNode immediateChildOne = new ProductionTreeNode(doesMatch);
        ProductionTreeNode immediateChildTwo = new ProductionTreeNode(doesNotMatch);
        ProductionTreeNode leaf = new ProductionTreeNode(doesMatch);

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        QueryResult result = root.query(new ProductionSelector("MATCH"));
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(List.of(root, immediateChildOne, leaf), result.getResult());
    }
}
