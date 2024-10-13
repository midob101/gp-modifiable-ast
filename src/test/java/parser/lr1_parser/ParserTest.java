package parser.lr1_parser;

import config_reader.ConfigReader;
import config_reader.ConfigReaderException;
import language_definitions.LanguageDefinition;
import lexer.Lexer;
import lexer.TokenList;
import lexer.exceptions.LexerParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test_utils.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ParserTest {

    private static Parser aabaabParser;
    private static Parser bracketsParser;
    private static Parser mathParser;
    private static Parser miniJavaParser;
    private static LanguageDefinition aabaabLanguageDefinition;
    private static LanguageDefinition bracketsLanguageDefinition;
    private static LanguageDefinition mathLanguageDefinition;
    private static LanguageDefinition miniJavaLanguageDefinition;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        aabaabLanguageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/aabaaab.txt"));
        aabaabParser = new Parser();
        aabaabParser.initialize(aabaabLanguageDefinition);

        bracketsLanguageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/brackets.txt"));
        bracketsParser = new Parser();
        bracketsParser.initialize(bracketsLanguageDefinition);

        mathLanguageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/math.txt"));
        mathParser = new Parser();
        mathParser.initialize(mathLanguageDefinition);

        miniJavaLanguageDefinition = ConfigReader.read(new File("src/main/resources/languages/minijava.txt"));
        miniJavaParser = new Parser();
        miniJavaParser.initialize(miniJavaLanguageDefinition);
    }

    @Test
    public void testParser() throws IOException, LexerParseException, ConfigReaderException {
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/aabaaab_valid.ab", aabaabLanguageDefinition);

        Assertions.assertTrue(aabaabParser.isValid(tokenList));
    }

    @Test
    public void testParser2() throws IOException, LexerParseException, ConfigReaderException {
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/aabaaab_invalid.ab", aabaabLanguageDefinition);

        Assertions.assertFalse(aabaabParser.isValid(tokenList));
    }

    @Test
    public void testParser3() throws IOException, LexerParseException, ConfigReaderException {
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/brackets.bracket", bracketsLanguageDefinition);

        Assertions.assertTrue(bracketsParser.isValid(tokenList));
    }

    @Test
    public void testParser4() throws IOException, LexerParseException, ConfigReaderException {
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/valid_calculation.math", mathLanguageDefinition);

        Assertions.assertTrue(mathParser.isValid(tokenList));
    }

    @ParameterizedTest(name="Test mini java {index} for source file {0}, should be {1}")
    @MethodSource("miniJavaProvider")
    public void testMiniJavaCst(String src, String comparisonFile) throws IOException, LexerParseException, ConfigReaderException {
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/minijava/" + src, miniJavaLanguageDefinition);

        String expected = Files.readString(Path.of("src/test/java/parser/lr1_parser/test_data/minijava/" + comparisonFile), StandardCharsets.UTF_8);
        Assertions.assertEquals(
                StringUtilities.trimEmptyLines(StringUtilities.useCRLF(expected)),
                StringUtilities.trimEmptyLines(StringUtilities.useCRLF(miniJavaParser.createCST(tokenList).toString()))
        );
    }

    @ParameterizedTest(name="Test mini java {index} for source file {0}, should be {1}")
    @MethodSource("invalidMiniJavaProvider")
    public void testInvalidMiniJavaCst(String src) throws IOException, LexerParseException, ConfigReaderException {
        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile("src/test/java/parser/lr1_parser/test_languages/minijava/" + src, miniJavaLanguageDefinition);

        Assertions.assertNull(miniJavaParser.createCST(tokenList));
    }

    private static Stream<Arguments> miniJavaProvider() {
        return Stream.of(
                Arguments.of("complete.minijava", "complete_cst.txt"),
                Arguments.of("only_main.minijava", "only_main_cst.txt")
        );
    }

    private static Stream<Arguments> invalidMiniJavaProvider() {
        return Stream.of(
                Arguments.of("main_with_return.minijava"),
                Arguments.of("invalid_math.minijava"),
                Arguments.of("main_after_other_class.minijava"),
                Arguments.of("empty.minijava")
        );
    }
}
