package lexer.custom_matchers;

import language_definitions.LanguageDefinition;
import lexer.ICustomMatcher;
import lexer.LexerContext;

/**
 * A custom implementation to match multiline comments.
 *
 * Comments are very hard to parse correctly with regex, therefore a custom implementation is easier.
 */
public class MultiLineCommentMatcher implements ICustomMatcher {
    @Override
    public String match(LexerContext context) {
        LanguageDefinition languageDefinition = context.getLanguageDefinition();
        if(languageDefinition.isMultiLineCommentAvailable()) {
            String multilineCommentStart  = languageDefinition.getMultiLineCommentStyleStart();
            String multilineCommentEnd = languageDefinition.getMultilineCommentStyleEnd();
            if(context.getNextNChars(multilineCommentStart.length()).equals(multilineCommentStart)) {
                // Find the multiline comment end.
                String remaining = context.getRemainingSource();
                int endPos = remaining.indexOf(multilineCommentEnd);
                return remaining.substring(0, endPos+multilineCommentEnd.length());
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "MultiLineCommentMatcher";
    }
}
