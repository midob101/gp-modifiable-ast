package syntax_tree;

import grammar.Production;
import grammar.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import selectors.data.ProductionSelector;
import syntax_tree.ast.AbstractSyntaxTreeNode;
import syntax_tree.ast.ProductionTreeNode;
import syntax_tree.ast.QueryResult;
import syntax_tree.ast.StringTreeNode;
import syntax_tree.ast.exceptions.AddingConnectedNode;
import test_utils.selector_stubs.TrueSelectorStub;

import java.util.List;

public class DeepCloneTest {

    @Test
    public void testDeepClone() throws AddingConnectedNode {
        Production doesNotMatch = Mockito.mock(Production.class);
        Mockito.when(doesNotMatch.leftHandSymbol()).thenReturn(new Symbol("DOES_NOT_MATCH", false));
        Production doesMatch = Mockito.mock(Production.class);
        Mockito.when(doesMatch.leftHandSymbol()).thenReturn(new Symbol("MATCH", false));


        ProductionTreeNode root = new ProductionTreeNode(doesMatch);
        ProductionTreeNode immediateChildOne = new ProductionTreeNode(doesMatch);
        ProductionTreeNode immediateChildTwo = new ProductionTreeNode(doesNotMatch);
        ProductionTreeNode leaf = new ProductionTreeNode(doesMatch);

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        AbstractSyntaxTreeNode rootClone = root.deepClone();

        Assertions.assertNotEquals(rootClone, root);
        Assertions.assertNotEquals(rootClone.getAllChildren().get(0), root.getAllChildren().get(0));
        Assertions.assertNotEquals(rootClone.getAllChildren().get(0).getAllChildren().get(0), root.getAllChildren().get(0).getAllChildren().get(0));
        Assertions.assertNotEquals(rootClone.getAllChildren().get(1), root.getAllChildren().get(1));
        Assertions.assertEquals(rootClone.getSourceCode(), root.getSourceCode());
    }

    @Test
    public void testDeepCloneRemovesParent() throws AddingConnectedNode {
        Production doesNotMatch = Mockito.mock(Production.class);
        Mockito.when(doesNotMatch.leftHandSymbol()).thenReturn(new Symbol("DOES_NOT_MATCH", false));
        Production doesMatch = Mockito.mock(Production.class);
        Mockito.when(doesMatch.leftHandSymbol()).thenReturn(new Symbol("MATCH", false));


        ProductionTreeNode root = new ProductionTreeNode(doesMatch);
        ProductionTreeNode immediateChildOne = new ProductionTreeNode(doesMatch);
        ProductionTreeNode immediateChildTwo = new ProductionTreeNode(doesNotMatch);
        ProductionTreeNode leaf = new ProductionTreeNode(doesMatch);

        root.addChild(immediateChildOne);
        root.addChild(immediateChildTwo);
        immediateChildOne.addChild(leaf);

        AbstractSyntaxTreeNode immediateChildOneClone = immediateChildOne.deepClone();

        Assertions.assertNotEquals(immediateChildOneClone, immediateChildOne);
        Assertions.assertNull(immediateChildOneClone.getParent());
        Assertions.assertNotNull(immediateChildOne.getParent());
    }
}
