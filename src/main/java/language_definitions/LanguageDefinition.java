package language_definitions;

/**
 * This file contains the configuration of a language
 * The values are injected by the json parser.
 */
public class LanguageDefinition {
    private String languageName;
    private String fileExtension;
    private boolean singleLineCommentAvailable;
    private String singleLineCommentStyle;
    private boolean multiLineCommentAvailable;
    private String multiLineCommentStyleStart;
    private String multilineCommentStyleEnd;
    private boolean caseSensitive;

    public String getLanguageName() {
        return languageName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public boolean isSingleLineCommentAvailable() {
        return singleLineCommentAvailable;
    }

    public String getSingleLineCommentStyle() {
        return singleLineCommentStyle;
    }

    public boolean isMultiLineCommentAvailable() {
        return multiLineCommentAvailable;
    }

    public String getMultiLineCommentStyleStart() {
        return multiLineCommentStyleStart;
    }

    public String getMultilineCommentStyleEnd() {
        return multilineCommentStyleEnd;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }
}
