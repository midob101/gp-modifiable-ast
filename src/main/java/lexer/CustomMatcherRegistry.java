package lexer;

import lexer.custom_matchers.MultiLineCommentMatcher;
import lexer.custom_matchers.SingleLineCommentMatcher;
import lexer.exceptions.LexerMissingCustomMatcherException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the custom matchers.
 *
 * There are global matchers, those are predefined and can be used for every language defined.
 * Also, there can be matchers defined for specific languages.
 */
public class CustomMatcherRegistry {
    private static final Map<String, ICustomMatcher> globalMatchers = Map.of(
            "singleLineCommentMatcher", new SingleLineCommentMatcher(),
            "multiLineCommentMatcher", new MultiLineCommentMatcher()
    );

    private static final Map<String, Map<String, ICustomMatcher>> languageMatchers = new HashMap<String, Map<String, ICustomMatcher>>();

    public static void registerCustomMatcher(String languageName, ICustomMatcher matcher) {
        languageMatchers.put(languageName, Map.of(languageName, matcher));
    }

    public static ICustomMatcher getMatcher(String languageName, String name) throws LexerMissingCustomMatcherException {
        ICustomMatcher globalMatcher = globalMatchers.get(name);
        ICustomMatcher languageMatcher = null;
        if(languageMatchers.containsKey(languageName)) {
            languageMatcher = languageMatchers.get(languageName).get(name);
        }

        if(languageMatcher != null) {
            return languageMatcher;
        }

        if(globalMatcher != null) {
            return globalMatcher;
        }

        throw new LexerMissingCustomMatcherException("Missing the custom matcher " + name + " for language " + languageName);
    }
}
