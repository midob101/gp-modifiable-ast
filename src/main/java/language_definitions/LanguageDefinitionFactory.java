package language_definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * This class is responsible to create the LanguageDefinition instances and adding them to the LanguageDefinitions
 * Map
 */
public class LanguageDefinitionFactory {
    public static LanguageDefinition createFromFile(String inputFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LanguageDefinition def = mapper.readValue(new File(inputFile), LanguageDefinition.class);
        LanguageDefinitions.addLanguageDefinition(def);
        return def;
    }
}
