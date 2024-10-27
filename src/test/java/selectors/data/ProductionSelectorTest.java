package selectors.data;

import grammar.GrammarRule;
import grammar.Symbol;
import lexer.LexerDefinition;
import lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import syntax_tree.ast.AbstractSyntaxTreeNode;
import syntax_tree.ast.ProductionTreeNode;
import syntax_tree.ast.StringTreeNode;
import syntax_tree.ast.TokenTreeNode;

public class ProductionSelectorTest {

    @Test
    public void testNotAProductionNode() {
        StringTreeNode node = new StringTreeNode("");
        ProductionSelector selector = new ProductionSelector("TEST");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testProductionNameDoesNotMatch() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST1", false));
        ProductionTreeNode node = new ProductionTreeNode(r1);
        ProductionSelector selector = new ProductionSelector("TEST");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchAlias() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST1", false));
        ProductionTreeNode node = new ProductionTreeNode(r1);
        node.setAlias("TEST1");
        ProductionSelector selector = new ProductionSelector("TEST");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchTokenName() {
        Token t1 = Mockito.mock(Token.class);
        LexerDefinition ld1 = Mockito.mock(LexerDefinition.class);
        Mockito.when(t1.getLexerDefinition()).thenReturn(ld1);
        Mockito.when(ld1.getName()).thenReturn("TEST");

        TokenTreeNode node = new TokenTreeNode(t1);
        ProductionSelector selector = new ProductionSelector("TEST");

        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesMatch() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST", false));
        ProductionTreeNode node = new ProductionTreeNode(r1);
        ProductionSelector selector = new ProductionSelector("TEST");
        Assertions.assertTrue(selector.matches(node));
    }
}
