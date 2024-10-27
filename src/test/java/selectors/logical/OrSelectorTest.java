package selectors.logical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test_selectors.FalseSelector;
import test_selectors.TrueSelector;
import syntax_tree.AbstractSyntaxTreeNode;

public class OrSelectorTest {

    @Test
    public void justOneSelectorFalse() {
        OrSelector selector = new OrSelector(new FalseSelector());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void justOneSelectorTrue() {
        OrSelector selector = new OrSelector(new TrueSelector());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllFalse() {
        OrSelector selector = new OrSelector(
                new FalseSelector(),
                new FalseSelector(),
                new FalseSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyLastTrue() {
        OrSelector selector = new OrSelector(
                new FalseSelector(),
                new FalseSelector(),
                new TrueSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyFirstTrue() {
        OrSelector selector = new OrSelector(
                new TrueSelector(),
                new FalseSelector(),
                new FalseSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsStartAndEndTrue() {
        OrSelector selector = new OrSelector(
                new TrueSelector(),
                new FalseSelector(),
                new TrueSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllTrue() {
        OrSelector selector = new OrSelector(
                new TrueSelector(),
                new TrueSelector(),
                new TrueSelector()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }
}
