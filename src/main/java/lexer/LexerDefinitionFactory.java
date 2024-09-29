package lexer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import language_definitions.LanguageDefinition;
import lexer.exceptions.LexerMissingCustomMatcherException;
import lexer.post_process.LexerPostProcessRegistry;

import java.io.File;
import java.io.IOException;

public class LexerDefinitionFactory {
    /**
     * Create a List of lexer definitions based on a given configuration file.
     */
    public static LexerDefinitionList createFromFile(LanguageDefinition languageDefinition, String inputFile) throws IOException, LexerMissingCustomMatcherException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(inputFile));
        JsonNode tokens = root.withArray("/tokens");
        LexerDefinitionList lexerDefinitionList = new LexerDefinitionList();
        for(int i = 0; i < tokens.size(); ++i) {
            JsonNode curToken = tokens.get(i);
            String name = curToken.get("name").asText();
            String pattern = curToken.has("pattern") ? curToken.get("pattern").asText() : "";
            String literal = curToken.has("literal") ? curToken.get("literal").asText() : "";
            boolean keepInAST = !curToken.has("keepInAST") || curToken.get("keepInAST").asBoolean();

            if(!pattern.isEmpty() || !literal.isEmpty()) {
                LexerDefinition t = new LexerDefinition(name, pattern, literal, keepInAST);
                lexerDefinitionList.addToken(t);
            }

            if(curToken.has("customMatcher")) {
                String customMatcherName = curToken.get("customMatcher").asText();
                ICustomMatcher customMatcher = CustomMatcherRegistry.getMatcher(languageDefinition.getLanguageName(), customMatcherName);
                LexerDefinition t = new LexerDefinition(name, customMatcher, keepInAST);
                lexerDefinitionList.addToken(t);
            }
        }

        JsonNode postProcessors = root.withArray("/postProcessors");
        for(int i = 0; i < postProcessors.size(); ++i) {
            LexerPostProcessRegistry.registerCustomMatcher(languageDefinition.getLanguageName(), postProcessors.get(i).asText());
        }

        return lexerDefinitionList;
    }
}
