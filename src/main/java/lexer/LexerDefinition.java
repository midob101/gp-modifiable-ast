package lexer;

/**
 * Instances of this class represent a definition of a lexer rule.
 *
 * There are three types of rules.
 *      1. Literals             --->      Plain text matches
 *      2. Patterns             --->      Regex matches
 *      3. Custom Matchers      --->      Custom implemented matchers
 */
public class LexerDefinition {
    private final String name;
    private String pattern = "";
    private String literal = "";
    private boolean keepInAST = true;
    private ICustomMatcher customMatcher = null;

    public LexerDefinition(String name, String pattern, String literal) {
        this.name = name;
        this.pattern = pattern;
        this.literal = literal;
        this.keepInAST = keepInAST;
    }

    public LexerDefinition(String name, ICustomMatcher customMatcher) {
        this.name = name;
        this.customMatcher = customMatcher;
        this.keepInAST = keepInAST;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public String getLiteral() {
        return literal;
    }

    public ICustomMatcher getCustomMatcher() {
        return customMatcher;
    }

    public boolean getKeepInAST() {
        return keepInAST;
    }

    public void setKeepInAST(boolean keepInAST) {
        this.keepInAST = keepInAST;
    }

    @Override
    public String toString() {
        return "LexerDefinition{" +
                "name='" + name + '\'' +
                ", pattern='" + pattern + '\'' +
                ", literal='" + literal + '\'' +
                ", customMatcher=" + customMatcher +
                '}';
    }
}
