package lexer;

import generic.FileUtils;
import language_definitions.LanguageDefinition;
import language_definitions.LanguageDefinitions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class contains the context of the lexer process of one single file.
 * This includes, but is not limited, the current position in the file and what tokens have been generated yet.
 *
 * This class also includes several utility functions, for example one to get the next n chars at the current position.
 */
public class LexerContext {

    /**
     * The source file the lexer is parsing
     */
    private final File source;

    /**
     * The entire source code in this file
     */
    private final String sourceCode;

    /**
     * The current position of the lexer in this file
     */
    private int parseIndex = 0;

    /**
     * The list of lexer definitions for the current language.
     */
    private final List<LexerDefinition> lexerDefinitionList;

    /**
     * The list of tokens generated by the lexer.
     */
    private final TokenList tokenList = new TokenList();

    public LexerContext(File source, List<LexerDefinition> lexerDefinitionList) throws IOException {
        this.source = source;
        this.sourceCode = Files.readString(Path.of(source.getPath()), StandardCharsets.UTF_8);
        this.lexerDefinitionList = lexerDefinitionList;
    }

    public File getSource() {
        return source;
    }

    public List<LexerDefinition> getLexerDefinitionList() {
        return lexerDefinitionList;
    }

    public int getParseIndex() {
        return parseIndex;
    }

    public void moveParseIndex(int offset) {
        this.parseIndex += offset;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    /**
     * Gets the current line number in the file.
     * This should only be used for debug and error messages.
     */
    public int getCurrentLineNumber() {
        String alreadyParsed = this.sourceCode.substring(0, parseIndex);
        String[] lines = alreadyParsed.split("\r\n|\r|\n");
        return lines.length;
    }

    /**
     * Gets the current column number in the file.
     * This should only be used in conjunction with the getCurrentLineNumber for output purposes.
     */
    public int getCurrentColumnNumberOfLine() {
        String alreadyParsed = this.sourceCode.substring(0, parseIndex);
        String[] lines = alreadyParsed.split("\r\n|\r|\n");
        return lines[lines.length-1].length() + 1;
    }

    /**
     * Checks if the parsing of this file is completed.
     */
    public boolean isDone() {
        return this.parseIndex >= this.sourceCode.length();
    }

    public void addToken(Token token) {
        this.tokenList.addToken(token);
    }

    public String getRemainingSource() {
        return this.sourceCode.substring(this.parseIndex);
    }

    public String getNextNChars(int n) {
        if(this.getRemainingSource().length() >= n) {
            return this.sourceCode.substring(this.parseIndex, this.parseIndex+n);
        }
        return "";
    }

    public TokenList getTokenList() {
        return tokenList;
    }

    public String getFileExtension() {
        return FileUtils.getExtension(this.source);
    }

    public LanguageDefinition getLanguageDefinition() {
        return LanguageDefinitions.getLanguageDefinitionByFileExtension(this.getFileExtension());
    }
}
