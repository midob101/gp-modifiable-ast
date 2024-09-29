package generic;

import language_definitions.LanguageDefinition;
import language_definitions.LanguageDefinitions;

import java.io.File;

public class FileUtils {

    public static String getExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static LanguageDefinition getLanguageDefinition(File file) {
        String extension = getExtension(file);
        return LanguageDefinitions.getLanguageDefinitionByFileExtension(extension);
    }

    public static LanguageDefinition getLanguageDefinition(String path) {
        String extension = getExtension(new File(path));
        return LanguageDefinitions.getLanguageDefinitionByFileExtension(extension);
    }
}
