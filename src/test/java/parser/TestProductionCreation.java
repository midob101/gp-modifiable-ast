package parser;

import config_reader.ConfigReader;
import config_reader.ConfigReaderException;
import language_definitions.LanguageDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test_utils.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestProductionCreation {

    @Test
    public void testCreation() throws IOException, ConfigReaderException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/languages/ab.txt"));
        String expected = Files.readString(Path.of("src/test/java/parser/results/ab.txt"), StandardCharsets.UTF_8);
        Assertions.assertEquals(StringUtilities.useCRLF(expected), StringUtilities.useCRLF(languageDefinition.getProductions().toString()));
    }
}
