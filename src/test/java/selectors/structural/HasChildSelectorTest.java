package selectors.structural;

import grammar.GrammarRule;
import lexer.LexerDefinition;
import lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import selectors.data.TokenSelector;
import syntax_tree.ast.ProductionTreeNode;
import syntax_tree.ast.TokenTreeNode;
import test_selectors.FalseSelector;
import test_selectors.TrueSelector;

public class HasChildSelectorTest {

    @Test
    public void testNoChildren() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        ProductionTreeNode root = new ProductionTreeNode(r1);
        HasChildSelector selector = new HasChildSelector(new TrueSelector());
        Assertions.assertFalse(selector.matches(root));
    }

    @Test
    public void testOneChildTrueSelector() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        GrammarRule r2 = Mockito.mock(GrammarRule.class);
        ProductionTreeNode root = new ProductionTreeNode(r1);
        ProductionTreeNode child = new ProductionTreeNode(r2);
        root.addChild(child);
        HasChildSelector selector = new HasChildSelector(new TrueSelector());
        Assertions.assertTrue(selector.matches(root));
    }

    @Test
    public void testOneChildFalseSelector() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        GrammarRule r2 = Mockito.mock(GrammarRule.class);
        ProductionTreeNode root = new ProductionTreeNode(r1);
        ProductionTreeNode child = new ProductionTreeNode(r2);
        root.addChild(child);
        HasChildSelector selector = new HasChildSelector(new FalseSelector());
        Assertions.assertFalse(selector.matches(root));
    }

    @Test
    public void testNestedChild() {
        GrammarRule production = Mockito.mock(GrammarRule.class);

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
