package lexer;

import language_definitions.LanguageDefinition;
import language_definitions.LanguageDefinitionFactory;
import lexer.exceptions.LexerMissingCustomMatcherException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LexerDefinitionFactoryTest {

    private LanguageDefinition languageDefinition;

    @Before
    public void setUp() throws Exception {
        languageDefinition = LanguageDefinitionFactory.createFromFile("src/test/java/example_language/language_definition.json");
    }

    @Test
    public void testTokenFactory() throws IOException, LexerMissingCustomMatcherException {
        LexerDefinitionList lexerDefinitionList = LexerDefinitionFactory.createFromFile(languageDefinition,"src/test/java/example_language/lexer_definitions.json");
        Assert.assertEquals(10, lexerDefinitionList.getDefinitionList().size());
        Assert.assertEquals("whitespace", lexerDefinitionList.getDefinitionList().get(0).getName());
        Assert.assertEquals("\\s+", lexerDefinitionList.getDefinitionList().get(0).getPattern());
        Assert.assertEquals("", lexerDefinitionList.getDefinitionList().get(0).getLiteral());
    }
}
