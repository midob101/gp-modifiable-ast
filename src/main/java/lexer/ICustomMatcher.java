package lexer;

/**
 * Interface for creating custom matchers.
 * A matcher is a class, that takes a lexer context and determines whether the first characters are matched by
 * this behaviour or not.
 *
 * This is used to create matchers that cannot be defined by literals or regex declarations in the lexer file.
 */
public interface ICustomMatcher {
    /**
     * Checks if the given lexer context matches a custom lexer definition.
     * Given the context, the current position in the source code can be determined.
     * The return value should be null if no match was detected, otherwise the matched string.
     */
    String match(LexerContext context);
}
