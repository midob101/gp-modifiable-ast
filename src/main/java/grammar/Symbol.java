package grammar;

public record Symbol(String name, boolean isTerminal) {
    public static final String INTERNAL_PARSE_IRRELEVANT_NAME = "INTERNAL_PARSE_IRELEVANT_NAME";
    public static final Symbol END_OF_INPUT = new Symbol("END_OF_INPUT", true);;
    public static final Symbol EPSILON = new Symbol("EPSILON", false);
    public static final Symbol INTERNAL_START_COPY = new Symbol("INTERNAL_START_COPY", true);
}
