package language_definitions;

import java.util.HashMap;

/**
 * This file is responsible to manage multiple language definitions and retrieve them based on the inputs given.
 */
public class LanguageDefinitions {
    private static final HashMap<String, LanguageDefinition> definitions = new HashMap<>();

    public static void addLanguageDefinition(LanguageDefinition definition) {
        definitions.put(definition.getFileExtension(), definition);
    }

    public static LanguageDefinition getLanguageDefinitionByFileExtension(String extension) {
        return definitions.get(extension);
    }
}
