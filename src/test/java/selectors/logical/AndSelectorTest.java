package selectors.logical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test_utils.selector_stubs.FalseSelectorStub;
import test_utils.selector_stubs.TrueSelectorStub;
import syntax_tree.ast.AbstractSyntaxTreeNode;

public class AndSelectorTest {

    @Test
    public void justOneSelectorFalse() {
        AndSelector selector = new AndSelector(new FalseSelectorStub());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void justOneSelectorTrue() {
        AndSelector selector = new AndSelector(new TrueSelectorStub());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllFalse() {
        AndSelector selector = new AndSelector(
                new FalseSelectorStub(),
                new FalseSelectorStub(),
                new FalseSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyLastTrue() {
        AndSelector selector = new AndSelector(
                new FalseSelectorStub(),
                new FalseSelectorStub(),
                new TrueSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyFirstTrue() {
        AndSelector selector = new AndSelector(
                new TrueSelectorStub(),
                new FalseSelectorStub(),
                new FalseSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsStartAndEndTrue() {
        AndSelector selector = new AndSelector(
                new TrueSelectorStub(),
                new FalseSelectorStub(),
                new TrueSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllTrue() {
        AndSelector selector = new AndSelector(
                new TrueSelectorStub(),
                new TrueSelectorStub(),
                new TrueSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }
}
