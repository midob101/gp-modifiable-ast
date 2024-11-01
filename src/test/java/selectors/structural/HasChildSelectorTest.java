package selectors.structural;

import grammar.Production;
import lexer.LexerDefinition;
import lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import selectors.data.TokenSelector;
import syntax_tree.ast.ProductionTreeNode;
import syntax_tree.ast.TokenTreeNode;
import syntax_tree.ast.exceptions.AddingConnectedNode;
import test_utils.selector_stubs.FalseSelectorStub;
import test_utils.selector_stubs.TrueSelectorStub;

public class HasChildSelectorTest {

    @Test
    public void testNoChildren() {
        Production r1 = Mockito.mock(Production.class);
        ProductionTreeNode root = new ProductionTreeNode(r1);
        HasChildSelector selector = new HasChildSelector(new TrueSelectorStub());
        Assertions.assertFalse(selector.matches(root));
    }

    @Test
    public void testOneChildTrueSelector() throws AddingConnectedNode {
        Production r1 = Mockito.mock(Production.class);
        Production r2 = Mockito.mock(Production.class);
        ProductionTreeNode root = new ProductionTreeNode(r1);
        ProductionTreeNode child = new ProductionTreeNode(r2);
        root.addChild(child);
        HasChildSelector selector = new HasChildSelector(new TrueSelectorStub());
        Assertions.assertTrue(selector.matches(root));
    }

    @Test
    public void testOneChildFalseSelector() throws AddingConnectedNode {
        Production r1 = Mockito.mock(Production.class);
        Production r2 = Mockito.mock(Production.class);
        ProductionTreeNode root = new ProductionTreeNode(r1);
        ProductionTreeNode child = new ProductionTreeNode(r2);
        root.addChild(child);
        HasChildSelector selector = new HasChildSelector(new FalseSelectorStub());
        Assertions.assertFalse(selector.matches(root));
    }

    @Test
    public void testNestedChild() throws AddingConnectedNode {
        Production production = Mockito.mock(Production.class);

        Token t1 = Mockito.mock(Token.class);
        LexerDefinition ld1 = Mockito.mock(LexerDefinition.class);
        Mockito.when(t1.getLexerDefinition()).thenReturn(ld1);
        Mockito.when(ld1.getName()).thenReturn("TEST");

        ProductionTreeNode root = new ProductionTreeNode(production);
        ProductionTreeNode firstLevelChild = new ProductionTreeNode(production);
        TokenTreeNode leafNode = new TokenTreeNode(t1);
        root.addChild(firstLevelChild);
        firstLevelChild.addChild(leafNode);
        HasChildSelector selector = new HasChildSelector(new TokenSelector("TEST"));

        Assertions.assertTrue(selector.matches(root));
    }
}
