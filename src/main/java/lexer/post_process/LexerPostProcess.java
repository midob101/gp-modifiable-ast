package lexer.post_process;

import lexer.LexerContext;
import java.util.Map;

public class LexerPostProcess {
    public static void run(LexerContext context) {
        Map<String, ILexerPostProcessor> postProcessors = LexerPostProcessRegistry.getPostProcessors(context.getLanguageDefinition().getLanguageName());

        for(ILexerPostProcessor postProcessor : postProcessors.values()) {
            postProcessor.run(context);
        }
    }
}
