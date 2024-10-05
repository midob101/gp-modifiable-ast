package lexer;

import config_reader.ConfigReader;
import language_definitions.LanguageDefinition;
import lexer.exceptions.LexerParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test_utils.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LexerTest {

    private LanguageDefinition languageDefinition;
    private LanguageDefinition whitespaceLanguageDefinition;

    @Before
    public void setUp() throws Exception {
        languageDefinition = ConfigReader.read(new File("src/test/java/languages/simple_prog_language.txt"));
        whitespaceLanguageDefinition = ConfigReader.read(new File("src/test/java/languages/whitespace_language.txt"));
    }

    @Test
    public void testWithoutComments() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/SimpleCode.ex", languageDefinition);
        Assert.assertEquals(35, t.getIterable().size());
        Assert.assertEquals("93.23", t.getIterable().get(4).getValue() );
        Assert.assertEquals("number", t.getIterable().get(4).getLexerDefinition().getName());
        String expected = Files.readString(Path.of("src/test/java/lexer/results/SimpleCode.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(StringUtilities.useCRLF(expected), StringUtilities.useCRLF(t.toString()));
    }

    @Test
    public void testInvalid() {
        Lexer lexer = new Lexer();
        LexerParseException exception = Assert.assertThrows(LexerParseException.class, () ->
                lexer.runForFile("src/test/java/lexer/test_data/Invalid.ex", languageDefinition));
        Assert.assertEquals("Failed to parse file. Line 4, Column 12", exception.getMessage());
    }

    @Test
    public void testComments() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/CommentsTest.ex", languageDefinition);
        Assert.assertEquals(12, t.getIterable().size());
        String expected = Files.readString(Path.of("src/test/java/lexer/results/CommentsTest.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(StringUtilities.useCRLF(expected), StringUtilities.useCRLF(t.toString()));
    }

    @Test
    public void testBackToSource() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/CommentsTest.ex", languageDefinition);
        String expected = Files.readString(Path.of("src/test/java/lexer/test_data/CommentsTest.ex"), StandardCharsets.UTF_8);
        Assert.assertEquals(expected, lexer.backToSource(t));
    }

    @Test
    public void testWhitespace() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/WhitespaceSimpleTest.exw", whitespaceLanguageDefinition);
        String expected = Files.readString(Path.of("src/test/java/lexer/results/WhitespaceSimpleTest.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(StringUtilities.useCRLF(expected), StringUtilities.useCRLF(t.toString()));
    }
}
