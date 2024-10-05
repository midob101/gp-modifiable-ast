package language_definitions;

import lexer.LexerDefinition;

import java.util.LinkedList;
import java.util.List;

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
    private final List<LexerDefinition> lexerDefinitionList = new LinkedList<>();

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

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setSingleLineCommentAvailable(boolean singleLineCommentAvailable) {
        this.singleLineCommentAvailable = singleLineCommentAvailable;
    }

    public void setSingleLineCommentStyle(String singleLineCommentStyle) {
        this.singleLineCommentStyle = singleLineCommentStyle;
    }

    public void setMultiLineCommentAvailable(boolean multiLineCommentAvailable) {
        this.multiLineCommentAvailable = multiLineCommentAvailable;
    }

    public void setMultiLineCommentStyleStart(String multiLineCommentStyleStart) {
        this.multiLineCommentStyleStart = multiLineCommentStyleStart;
    }

    public void setMultilineCommentStyleEnd(String multilineCommentStyleEnd) {
        this.multilineCommentStyleEnd = multilineCommentStyleEnd;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public void addLexerDefinition(LexerDefinition lexerDefinition) {
        lexerDefinitionList.add(lexerDefinition);
    }

    public List<LexerDefinition> getLexerDefinitionList() {
        return lexerDefinitionList;
    }
}
