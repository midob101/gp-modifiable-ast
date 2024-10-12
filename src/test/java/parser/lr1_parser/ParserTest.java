package parser.lr1_parser;

import config_reader.ConfigReader;
import config_reader.ConfigReaderException;
import language_definitions.LanguageDefinition;
import lexer.Lexer;
import lexer.TokenList;
import lexer.exceptions.LexerParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

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

    @ParameterizedTest(name="Test mini java {index} for source file {0}, should be {1}")
    @MethodSource("miniJavaProvider")
    public void testMiniJava(String src, boolean isValid) throws IOException, LexerParseException, ConfigReaderException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/main/resources/languages/minijava.txt"));
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/minijava/" + src, languageDefinition);

        Assertions.assertEquals(isValid, Parser.isValid(tokenList, languageDefinition));
    }

    private static Stream<Arguments> miniJavaProvider() {
        return Stream.of(
                Arguments.of("complete.minijava", true),
                Arguments.of("only_main.minijava", true),
                Arguments.of("main_with_return.minijava", false),
                Arguments.of("invalid_math.minijava", false),
                Arguments.of("main_after_other_class.minijava", false),
                Arguments.of("empty.minijava", false)
        );
    }
}
