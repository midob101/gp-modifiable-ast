package selectors.data;

import grammar.GrammarRule;
import grammar.Symbol;
import lexer.LexerDefinition;
import lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import syntax_tree.AbstractSyntaxTreeNode;

public class ProductionSelectorTest {

    @Test
    public void testNotAProductionNode() {
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode("");
        ProductionSelector selector = new ProductionSelector("TEST");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testProductionNameDoesNotMatch() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST1", false));
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(r1);
        ProductionSelector selector = new ProductionSelector("TEST");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchAlias() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST1", false));
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(r1);
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

        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(t1);
        ProductionSelector selector = new ProductionSelector("TEST");

        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesMatch() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST", false));
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(r1);
        ProductionSelector selector = new ProductionSelector("TEST");
        Assertions.assertTrue(selector.matches(node));
    }
}
