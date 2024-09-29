package lexer;

public interface ICustomMatcher {
    /**
     * Checks if the given lexer context matches a custom lexer definition.
     * Given the context, the current position in the source code can be determined.
     * The return value should be an empty string if no match was detected or the matched string.
     *
     * @param context
     * @return String
     */
    public String match(LexerContext context);
}
