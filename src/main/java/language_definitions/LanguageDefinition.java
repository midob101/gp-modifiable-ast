package language_definitions;

/**
 * This file contains the configuration of a language
 * The values are injected by the json parser.
 */
public class LanguageDefinition {
    @SuppressWarnings("unused")
    private String languageName;
    @SuppressWarnings("unused")
    private String fileExtension;
    @SuppressWarnings("unused")
    private boolean singleLineCommentAvailable;
    @SuppressWarnings("unused")
    private String singleLineCommentStyle;
    @SuppressWarnings("unused")
    private boolean multiLineCommentAvailable;
    @SuppressWarnings("unused")
    private String multiLineCommentStyleStart;
    @SuppressWarnings("unused")
    private String multilineCommentStyleEnd;
    @SuppressWarnings("unused")
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
