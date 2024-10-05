package parser.lr1_parser;

import config_reader.ConfigReader;
import language_definitions.LanguageDefinition;
import lexer.Lexer;
import lexer.TokenList;
import lexer.exceptions.LexerParseException;
import org.junit.Assert;
import org.junit.Test;
import parser.lr1_parser.items.Parser;

import java.io.File;
import java.io.IOException;

public class ParserTest {

    @Test
    public void testParser() throws IOException, LexerParseException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/aabaaab.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/aabaaab_valid.ab", languageDefinition);

        Assert.assertTrue(Parser.isValid(tokenList, languageDefinition));
    }

    @Test
    public void testParser2() throws IOException, LexerParseException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/aabaaab.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/aabaaab_invalid.ab", languageDefinition);

        Assert.assertFalse(Parser.isValid(tokenList, languageDefinition));
    }
}
