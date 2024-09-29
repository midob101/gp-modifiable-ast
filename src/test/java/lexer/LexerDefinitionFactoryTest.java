package lexer;

import lexer.exceptions.LexerMissingCustomMatcherException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class LexerDefinitionFactoryTest {

    @Test
    public void testTokenFactory() throws IOException, LexerMissingCustomMatcherException {
        LexerDefinitionList lexerDefinitionList = LexerDefinitionFactory.createFromFile("src/test/java/example_language/lexer_definitions.json");
        Assert.assertEquals(10, lexerDefinitionList.getDefinitionList().size());
        Assert.assertEquals("whitespace", lexerDefinitionList.getDefinitionList().get(0).getName());
        Assert.assertEquals("\\s+", lexerDefinitionList.getDefinitionList().get(0).getPattern());
        Assert.assertEquals("", lexerDefinitionList.getDefinitionList().get(0).getLiteral());
    }
}
