package parser;

import config_reader.ConfigReader;
import language_definitions.LanguageDefinition;
import org.junit.Assert;
import org.junit.Test;
import test_utils.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestGrammarRuleCreation {

    @Test
    public void testCreation() throws IOException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/languages/ab.txt"));
        String expected = Files.readString(Path.of("src/test/java/parser/results/ab.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(StringUtilities.useCRLF(expected), StringUtilities.useCRLF(languageDefinition.getGrammarRules().toString()));
    }
}
