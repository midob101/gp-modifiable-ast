package config_reader;

import grammar.GrammarRule;
import grammar.Symbol;
import language_definitions.LanguageDefinition;
import language_definitions.LanguageDefinitions;
import lexer.CustomMatcherRegistry;
import lexer.ICustomMatcher;
import lexer.LexerDefinition;
import lexer.exceptions.LexerMissingCustomMatcherException;
import lexer.post_process.LexerPostProcessRegistry;
import logger.Logger;
import logger.LoggerComponents;

import java.io.*;
import java.util.*;

/**
 * This class is responsible for reading the configuration of a language from a given configuration file.
 */
public class ConfigReader {
    /**
     * Initiates the read process
     */
    public static LanguageDefinition read(File file) throws ConfigReaderException {
        Logger.debug(LoggerComponents.CONFIG_READER, "Reading language definitions from " + file);
        String segment = "";
        String concatted = ""; // Keeps track of the current statement, it can span over multiple lines.
                               // Gets cleared either on segment change or if the line ends with a semicolon
        LanguageDefinition resultLanguage = new LanguageDefinition();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // First check if the line includes a segment change.
                boolean segmentChanged = false;
                switch(line) {
                    case "LANGUAGE_DEF":
                    case "LEXER_RULES":
                    case "HIDDEN_LEXER_RULES":
                    case "LEXER_POSTPROCESSORS":
                    case "GRAMMAR_RULES":
                        segment = line;
                        segmentChanged = true;
                        break;
                }
                if(segmentChanged) {
                    Logger.debug(LoggerComponents.CONFIG_READER, "Segment was changed, new segment " + segment);
                    if(!concatted.isEmpty()) {
                        Logger.err(LoggerComponents.CONFIG_READER, "Segment was changed, even though the last line was not finished. " + line);
                    }
                    concatted = "";
                    continue;
                }
                // No segment change was detected.
                if(line.trim().endsWith(";")) {
                    // Line ends with a semicolon, all data for this configuration has been collected.
                    concatted = concatted + line.substring(0, line.length() - 1);

                    // Depending on the segment, trigger a different action
                    switch(segment) {
                        case "LANGUAGE_DEF":
                            ConfigReader.readLanguageDefinitionValue(concatted, resultLanguage);
                            break;
                        case "LEXER_RULES":
                            ConfigReader.readLexerRule(concatted, resultLanguage);
                            break;
                        case "HIDDEN_LEXER_RULES":
                            ConfigReader.readHiddenLexerDefinitions(concatted, resultLanguage);
                            break;
                        case "LEXER_POSTPROCESSORS":
                            ConfigReader.readLexerPostprocessors(concatted, resultLanguage);
                            break;
                        case "GRAMMAR_RULES":
                            ConfigReader.readGrammarRules(concatted, resultLanguage);
                            break;
                    }

                    concatted = "";
                } else if (!line.trim().isEmpty()) {
                    // Append the current line to the concatted data.
                    concatted = concatted + line;
                }
            }
        } catch (IOException | LexerMissingCustomMatcherException e) {
            throw new ConfigReaderException(e);
        }
        LanguageDefinitions.addLanguageDefinition(resultLanguage);
        if(!resultLanguage.isValid()) {
            throw new ConfigReaderException("Invalid language definition.");
        }
        return resultLanguage;
    }

    /**
     * Sets the corresponding language definition setting based on the current line in the configuration file.
     */
    private static void readLanguageDefinitionValue(String line, LanguageDefinition languageDefinition) {
        String[] configParts = line.split("=");
        if(configParts.length != 2) {
            Logger.err(LoggerComponents.CONFIG_READER,"Invalid config line: " + line);
        }
        String key = configParts[0].trim();
        String value = configParts[1].trim();

        switch (key) {
            case "name":
                languageDefinition.setLanguageName(value);
                break;
            case "file_extension":
                languageDefinition.setFileExtension(value);
                break;
            case "single_line_comment_available":
                languageDefinition.setSingleLineCommentAvailable(Boolean.parseBoolean(value));
                break;
            case "multi_line_comment_available":
                languageDefinition.setMultiLineCommentAvailable(Boolean.parseBoolean(value));
                break;
            case "single_line_comment_style":
                languageDefinition.setSingleLineCommentStyle(value);
                break;
            case "multi_line_comment_style_start":
                languageDefinition.setMultiLineCommentStyleStart(value);
                break;
            case "multi_line_comment_style_end":
                languageDefinition.setMultilineCommentStyleEnd(value);
                break;
            case "case_sensitive":
                languageDefinition.setCaseSensitive(Boolean.parseBoolean(value));
                break;
            case "grammar_start":
                languageDefinition.setGrammarStartSymbol(new Symbol(value, false));
                break;
        }
    }

    /**
     * Create a new lexer definition rule based on the current line
     */
    private static void readLexerRule(String definition, LanguageDefinition languageDefinition) throws LexerMissingCustomMatcherException {
        String[] configParts = definition.split("=", 2);
        if(configParts.length != 2) {
            Logger.err(LoggerComponents.CONFIG_READER,"Invalid config line: " + definition);
        }
        String lexerDefinitionName = configParts[0].trim();
        String value = configParts[1].trim();

        LexerDefinition newDefinition = null;
        if(value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            newDefinition = new LexerDefinition(lexerDefinitionName, "", value);
        }

        if(value.startsWith("regex(") && value.endsWith(")")) {
            value = value.substring(6, value.length() - 1);
            newDefinition =  new LexerDefinition(lexerDefinitionName, value, "");
        }

        if(value.startsWith("customMatcher(") && value.endsWith(")")) {
            value = value.substring(14, value.length() - 1);
            ICustomMatcher customMatcher = CustomMatcherRegistry.getMatcher(languageDefinition.getLanguageName(), value);
            newDefinition =  new LexerDefinition(lexerDefinitionName, customMatcher);
        }

        if(newDefinition == null) {
            Logger.err(LoggerComponents.CONFIG_READER,"Failed to determine the process to parse lexer definition " + definition);
        } else {
            languageDefinition.addLexerDefinition(newDefinition);
        }
    }

    /**
     * Marks the lexer definition as hidden, if their given entry is in the HIDDEN_LEXER_RULES segment
     */
    private static void readHiddenLexerDefinitions(String definition, LanguageDefinition languageDefinition) {
        String[] hiddenDefinitions = definition.split(",");
        for(LexerDefinition lexerDefinition: languageDefinition.getLexerDefinitionList()) {
            Optional<String> match = Arrays.stream(hiddenDefinitions)
                    .filter((String curDefinition) -> curDefinition.trim().equals(lexerDefinition.getName()))
                    .findAny();
            if(match.isPresent()) {
                lexerDefinition.setKeepInAST(false);
            }
        }
    }

    /**
     * Registers the custom lexer postprocessors for the given file.
     */
    private static void readLexerPostprocessors(String list, LanguageDefinition resultLanguage) {
        String[] postprocessors = list.split(",");
        for(String postprocessor: postprocessors) {
            LexerPostProcessRegistry.registerCustomMatcher(resultLanguage.getLanguageName(), postprocessor.trim());
        }
    }

    /**
     * Creates the grammar rules for a definition
     */
    private static void readGrammarRules(String concatted, LanguageDefinition resultLanguage) {
        String[] parts = concatted.split("->");
        String name = parts[0].trim();
        Symbol leftHandSideSymbol = new Symbol(name, false);
        String[] ruleDefinitions = parts[1].split("\\|");
        for(String ruleDefinition: ruleDefinitions) {
            List<Symbol> symbols = new LinkedList<>();
            ruleDefinition = ruleDefinition.trim().replaceAll("\\s+", " ");
            String[] symbolsAsString = ruleDefinition.split("\\s+");
            for(String symbolName: symbolsAsString) {
                if(symbolName.equals("EPSILON")) {
                    //symbols.add(Symbol.EPSILON);
                } else {
                    boolean isTerminal = false;
                    if(symbolName.matches("[a-z_0-9]+")) {
                        isTerminal = true;
                    }
                    symbols.add(new Symbol(symbolName, isTerminal));
                }
            }

            GrammarRule grammarRule = new GrammarRule(leftHandSideSymbol, symbols);
            resultLanguage.addGrammarRule(grammarRule);
        }
    }
}
