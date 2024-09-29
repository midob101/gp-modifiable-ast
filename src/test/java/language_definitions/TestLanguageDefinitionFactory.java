package language_definitions;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

public class TestLanguageDefinitionFactory {

    @Test
    public void testCreationByFile() throws IOException {
        LanguageDefinition languageDefinition = LanguageDefinitionFactory.createFromFile("src/test/java/example_language/language_definition.json");
        Assert.assertEquals("Example Language", languageDefinition.getLanguageName());
        Assert.assertEquals("//", languageDefinition.getSingleLineCommentStyle());
        Assert.assertEquals(true, languageDefinition.isSingleLineCommentAvailable());
        Assert.assertEquals(LanguageDefinitions.getLanguageDefinitionByFileExtension("ex"), languageDefinition);
    }
}
