package lexer.custom_matchers;

import language_definitions.LanguageDefinition;
import lexer.ICustomMatcher;
import lexer.LexerContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A custom implementation to match single line comments. The matched token includes the newline.
 * This is done, so we do not accidentally remove the newline when modifying the AST.
 *
 * Comments are very hard to parse correctly with regex, therefore a custom implementation is easier.
 */
public class SingleLineCommentMatcher implements ICustomMatcher {
    @Override
    public String match(LexerContext context) {
        LanguageDefinition languageDefinition = context.getLanguageDefinition();

        if(languageDefinition.isSingleLineCommentAvailable()) {
            String singleLineCommentStyle = languageDefinition.getSingleLineCommentStyle();
            if(context.getNextNChars(singleLineCommentStyle.length()).equals(singleLineCommentStyle)) {
                // Find the end of line. This will be the comment token
                String remaining = context.getRemainingSource();
                Pattern p = Pattern.compile(".*(\r\n|\r|\n|$)");
                Matcher matcher = p.matcher(remaining);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "SingleLineCommentMatcher";
    }
}
