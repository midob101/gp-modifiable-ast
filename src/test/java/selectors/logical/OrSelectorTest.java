package selectors.logical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test_utils.selector_stubs.FalseSelectorStub;
import test_utils.selector_stubs.TrueSelectorStub;
import syntax_tree.ast.AbstractSyntaxTreeNode;

public class OrSelectorTest {

    @Test
    public void justOneSelectorFalse() {
        OrSelector selector = new OrSelector(new FalseSelectorStub());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void justOneSelectorTrue() {
        OrSelector selector = new OrSelector(new TrueSelectorStub());
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllFalse() {
        OrSelector selector = new OrSelector(
                new FalseSelectorStub(),
                new FalseSelectorStub(),
                new FalseSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertFalse(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyLastTrue() {
        OrSelector selector = new OrSelector(
                new FalseSelectorStub(),
                new FalseSelectorStub(),
                new TrueSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsOnlyFirstTrue() {
        OrSelector selector = new OrSelector(
                new TrueSelectorStub(),
                new FalseSelectorStub(),
                new FalseSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsStartAndEndTrue() {
        OrSelector selector = new OrSelector(
                new TrueSelectorStub(),
                new FalseSelectorStub(),
                new TrueSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }

    @Test
    public void multipleSelectorsAllTrue() {
        OrSelector selector = new OrSelector(
                new TrueSelectorStub(),
                new TrueSelectorStub(),
                new TrueSelectorStub()
        );
        AbstractSyntaxTreeNode node = Mockito.mock(AbstractSyntaxTreeNode.class);
        Assertions.assertTrue(selector.matches(node));
    }
}
