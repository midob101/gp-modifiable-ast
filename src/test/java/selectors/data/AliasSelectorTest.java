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

public class AliasSelectorTest {

    @Test
    public void testNoAliasDefined() {
        Production r1 = Mockito.mock(Production.class);
        ProductionTreeNode node = new ProductionTreeNode(r1);
        AliasSelector selector = new AliasSelector("test");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testNoMatchingAliasDefined() {
        Production r1 = Mockito.mock(Production.class);
        ProductionTreeNode node = new ProductionTreeNode(r1);
        node.addAlias("test1");
        AliasSelector selector = new AliasSelector("test");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testMatchingAliasDefined() {
        Production r1 = Mockito.mock(Production.class);
        ProductionTreeNode node = new ProductionTreeNode(r1);
        node.addAlias("test");
        AliasSelector selector = new AliasSelector("test");
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchProductionName() {
        Production r1 = Mockito.mock(Production.class);
        Mockito.when(r1.leftHandSymbol()).thenReturn(new Symbol("TEST", false));
        ProductionTreeNode node = new ProductionTreeNode(r1);
        AliasSelector selector = new AliasSelector("TEST");
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void testDoesNotMatchTokenName() {
        Token t1 = Mockito.mock(Token.class);
        LexerDefinition ld1 = Mockito.mock(LexerDefinition.class);
        Mockito.when(t1.getLexerDefinition()).thenReturn(ld1);
        Mockito.when(ld1.getName()).thenReturn("TEST");

        TokenTreeNode node = new TokenTreeNode(t1);
        AliasSelector selector = new AliasSelector("TEST");

        Assertions.assertFalse(selector.matches(node));
    }
}
