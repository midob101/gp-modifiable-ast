package lexer.post_process;

import java.util.HashMap;
import java.util.Map;

public class LexerPostProcessRegistry {
    private static final Map<String, Map<String, ILexerPostProcessor>> postProcessors = new HashMap<>();

    public static void registerCustomMatcher(String languageName, ILexerPostProcessor postProcessor) {
        postProcessors.put(languageName, Map.of(languageName, postProcessor));
    }

    public static void registerCustomMatcher(String languageName, String postProcessor) {
        if(postProcessor.equals("keepWhitespacesSOL")) {
            postProcessors.put(languageName, Map.of(languageName, new KeepWhitespacesSOL()));
        }
    }

    public static Map<String, ILexerPostProcessor> getPostProcessors(String languageName) {
        return postProcessors.getOrDefault(languageName, new HashMap<>());
    }
}
