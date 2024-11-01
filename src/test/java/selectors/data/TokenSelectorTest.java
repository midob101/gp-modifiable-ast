package selectors.data;

import grammar.Production;
import grammar.Symbol;
import lexer.LexerDefinition;
import lexer.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import syntax_tree.ast.ProductionTreeNode;
import syntax_tree.ast.TokenTreeNode;

public class TokenSelectorTest {

    @Test
    public void testProductionNode() {
        Production r1 = Mockito.mock(Production.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("test", false));
        ProductionTreeNode node = new ProductionTreeNode(r1);
        TokenSelector selector = new TokenSelector("test");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testLexerDefinitionNameDoesNotMatch() {
        Token t1 = Mockito.mock(Token.class);
        LexerDefinition l1 = Mockito.mock(LexerDefinition.class);
        Mockito.when(t1.getLexerDefinition()).thenReturn(l1);
        Mockito.when(l1.getName()).thenReturn("test_definition");
        TokenTreeNode node = new TokenTreeNode(t1);
        TokenSelector selector = new TokenSelector("test");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchAlias() {
        Production r1 = Mockito.mock(Production.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("test1", false));
        ProductionTreeNode node = new ProductionTreeNode(r1);
        node.setAlias("test1");
        TokenSelector selector = new TokenSelector("test1");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesMatchTokenName() {
        Token t1 = Mockito.mock(Token.class);
        LexerDefinition l1 = Mockito.mock(LexerDefinition.class);
        Mockito.when(t1.getLexerDefinition()).thenReturn(l1);
        Mockito.when(l1.getName()).thenReturn("test");

        TokenTreeNode node = new TokenTreeNode(t1);
        TokenSelector selector = new TokenSelector("test");

        Assertions.assertTrue(selector.matches(node));
    }
}
