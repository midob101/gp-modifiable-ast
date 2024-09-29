package lexer;

import language_definitions.LanguageDefinition;
import language_definitions.LanguageDefinitionFactory;
import lexer.exceptions.LexerParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LexerTest {

    private LexerDefinitionList lexerDefinitionList;
    private LexerDefinitionList lexerDefinitionListWhitespace;

    @Before
    public void setUp() throws Exception {
        LanguageDefinition languageDefinition = LanguageDefinitionFactory.createFromFile("src/test/java/example_language/language_definition.json");
        lexerDefinitionList = LexerDefinitionFactory.createFromFile(languageDefinition, "src/test/java/example_language/lexer_definitions.json");

        languageDefinition = LanguageDefinitionFactory.createFromFile("src/test/java/whitespace_language/language_definition.json");
        lexerDefinitionListWhitespace = LexerDefinitionFactory.createFromFile(languageDefinition, "src/test/java/whitespace_language/lexer_definitions.json");
    }

    @Test
    public void testWithoutComments() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/SimpleCode.ex", lexerDefinitionList);
        Assert.assertEquals(35, t.getTokenList().size());
        Assert.assertEquals("93.23", t.getTokenList().get(4).getValue() );
        Assert.assertEquals("number", t.getTokenList().get(4).getLexerDefinition().getName());
        String expected = Files.readString(Path.of("src/test/java/lexer/results/SimpleCode.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(expected, t.toString());
    }

    @Test
    public void testInvalid() {
        Lexer lexer = new Lexer();
        LexerParseException exception = Assert.assertThrows(LexerParseException.class, () ->
                lexer.runForFile("src/test/java/lexer/test_data/Invalid.ex", lexerDefinitionList));
        Assert.assertEquals("Failed to parse file. Line 4, Column 12", exception.getMessage());
    }

    @Test
    public void testComments() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/CommentsTest.ex", lexerDefinitionList);
        Assert.assertEquals(12, t.getTokenList().size());
        String expected = Files.readString(Path.of("src/test/java/lexer/results/CommentsTest.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(expected, t.toString());
    }

    @Test
    public void testBackToSource() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/CommentsTest.ex", lexerDefinitionList);
        String expected = Files.readString(Path.of("src/test/java/lexer/test_data/CommentsTest.ex"), StandardCharsets.UTF_8);
        Assert.assertEquals(expected, lexer.backToSource(t));
    }

    @Test
    public void testWhitespace() throws IOException, LexerParseException {
        Lexer lexer = new Lexer();
        TokenList t = lexer.runForFile("src/test/java/lexer/test_data/WhitespaceSimpleTest.ex", lexerDefinitionListWhitespace);
        String expected = Files.readString(Path.of("src/test/java/lexer/results/WhitespaceSimpleTest.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(expected, t.toString());
    }
}
