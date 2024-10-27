package selectors.logical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test_selectors.FalseSelector;
import test_selectors.TrueSelector;
import syntax_tree.AbstractSyntaxTreeNode;

public class AndSelectorTest {

    @Test
    public void justOneSelectorFalse() {
        AndSelector selector = new AndSelector(new FalseSelector());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void justOneSelectorTrue() {
        AndSelector selector = new AndSelector(new TrueSelector());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllFalse() {
        AndSelector selector = new AndSelector(
                new FalseSelector(),
                new FalseSelector(),
                new FalseSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyLastTrue() {
        AndSelector selector = new AndSelector(
                new FalseSelector(),
                new FalseSelector(),
                new TrueSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyFirstTrue() {
        AndSelector selector = new AndSelector(
                new TrueSelector(),
                new FalseSelector(),
                new FalseSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsStartAndEndTrue() {
        AndSelector selector = new AndSelector(
                new TrueSelector(),
                new FalseSelector(),
                new TrueSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllTrue() {
        AndSelector selector = new AndSelector(
                new TrueSelector(),
                new TrueSelector(),
                new TrueSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }
}
