package lexer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lexer.custom_matchers.MultiLineCommentMatcher;
import lexer.custom_matchers.SingleLineCommentMatcher;
import lexer.exceptions.LexerMissingCustomMatcherException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LexerDefinitionFactory {
    private static final Map<String, ICustomMatcher> customMatcherMap = Map.of(
        "singleLineCommentMatcher", new SingleLineCommentMatcher(),
        "multiLineCommentMatcher", new MultiLineCommentMatcher()
    );

    /**
     * Create a List of lexer definitions based on a given configuration file.
     */
    public static LexerDefinitionList createFromFile(String inputFile) throws IOException, LexerMissingCustomMatcherException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(inputFile));
        JsonNode tokens = root.withArray("/tokens");
        LexerDefinitionList lexerDefinitionList = new LexerDefinitionList();
        for(int i = 0; i < tokens.size(); ++i) {
            JsonNode curToken = tokens.get(i);
            String name = curToken.get("name").asText();
            String pattern = curToken.has("pattern") ? curToken.get("pattern").asText() : "";
            String literal = curToken.has("literal") ? curToken.get("literal").asText() : "";
            if(!pattern.isEmpty() || !literal.isEmpty()) {
                LexerDefinition t = new LexerDefinition(name, pattern, literal);
                lexerDefinitionList.addToken(t);
            }
            if(curToken.has("customMatcher")) {
                String customMatcher = curToken.get("customMatcher").asText();
                if(!customMatcherMap.containsKey(customMatcher)) {
                    throw new LexerMissingCustomMatcherException("Missing the custom matcher for " + customMatcher);
                }
                LexerDefinition t = new LexerDefinition(name, customMatcherMap.get(customMatcher));
                lexerDefinitionList.addToken(t);
            };
        }
        return lexerDefinitionList;
    }
}
