package parser.lr1_parser;

import config_reader.ConfigReader;
import config_reader.ConfigReaderException;
import language_definitions.LanguageDefinition;
import lexer.Lexer;
import lexer.TokenList;
import lexer.exceptions.LexerParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ParserTest {

    @Test
    public void testParser() throws IOException, LexerParseException, ConfigReaderException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/aabaaab.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/aabaaab_valid.ab", languageDefinition);

        Assertions.assertTrue(Parser.isValid(tokenList, languageDefinition));
    }

    @Test
    public void testParser2() throws IOException, LexerParseException, ConfigReaderException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/aabaaab.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/aabaaab_invalid.ab", languageDefinition);

        Assertions.assertFalse(Parser.isValid(tokenList, languageDefinition));
    }

    @Test
    public void testParser3() throws IOException, LexerParseException, ConfigReaderException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/brackets.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/brackets.bracket", languageDefinition);

        Assertions.assertTrue(Parser.isValid(tokenList, languageDefinition));
    }

    @Test
    public void testParser4() throws IOException, LexerParseException, ConfigReaderException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/math.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/valid_calculation.math", languageDefinition);

        Assertions.assertTrue(Parser.isValid(tokenList, languageDefinition));
    }

    @Test
    public void testMiniJava() throws IOException, LexerParseException, ConfigReaderException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/main/resources/languages/minijava.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/minijava.java", languageDefinition);

        Assertions.assertTrue(Parser.isValid(tokenList, languageDefinition));
    }
}
