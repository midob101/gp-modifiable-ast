package selectors.structural;

import grammar.GrammarRule;
import lexer.LexerDefinition;
import lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import selectors.data.TokenSelector;
import test_selectors.FalseSelector;
import test_selectors.TrueSelector;
import syntax_tree.AbstractSyntaxTreeNode;

public class HasChildSelectorTest {

    @Test
    public void testNoChildren() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode(r1);
        HasChildSelector selector = new HasChildSelector(new TrueSelector());
        Assertions.assertFalse(selector.matches(root));
    }

    @Test
    public void testOneChildTrueSelector() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        GrammarRule r2 = Mockito.mock(GrammarRule.class);
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode(r1);
        AbstractSyntaxTreeNode child = new AbstractSyntaxTreeNode(r2);
        root.addChild(child);
        HasChildSelector selector = new HasChildSelector(new TrueSelector());
        Assertions.assertTrue(selector.matches(root));
    }

    @Test
    public void testOneChildFalseSelector() {
        GrammarRule r1 = Mockito.mock(GrammarRule.class);
        GrammarRule r2 = Mockito.mock(GrammarRule.class);
        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode(r1);
        AbstractSyntaxTreeNode child = new AbstractSyntaxTreeNode(r2);
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

        AbstractSyntaxTreeNode root = new AbstractSyntaxTreeNode(production);
        AbstractSyntaxTreeNode firstLevelChild = new AbstractSyntaxTreeNode(production);
        AbstractSyntaxTreeNode leafNode = new AbstractSyntaxTreeNode(t1);
        root.addChild(firstLevelChild);
        firstLevelChild.addChild(leafNode);
        HasChildSelector selector = new HasChildSelector(new TokenSelector("TEST"));

        Assertions.assertTrue(selector.matches(root));
    }
}
