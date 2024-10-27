package selectors.data;

import grammar.GrammarRule;
import grammar.Symbol;
import lexer.LexerDefinition;
import lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import syntax_tree.AbstractSyntaxTreeNode;

public class AliasSelectorTest {

    @Test
    public void testNoAliasDefined() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(r1);
        AliasSelector selector = new AliasSelector("test");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testNoMatchingAliasDefined() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(r1);
        node.setAlias("test1");
        AliasSelector selector = new AliasSelector("test");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testMatchingAliasDefined() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(r1);
        node.setAlias("test");
        AliasSelector selector = new AliasSelector("test");
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchProductionName() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST", false));
        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(r1);
        AliasSelector selector = new AliasSelector("TEST");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchTokenName() {
        Token t1 = Mockito.mock(Token.class);
        LexerDefinition ld1 = Mockito.mock(LexerDefinition.class);
        Mockito.when(t1.getLexerDefinition()).thenReturn(ld1);
        Mockito.when(ld1.getName()).thenReturn("TEST");

        AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode(t1);
        AliasSelector selector = new AliasSelector("TEST");

        Assertions.assertFalse(selector.matches(node));
    }
}
