package grammar;

public record Symbol(String name, boolean isTerminal) {
    public static final Symbol END_OF_INPUT = new Symbol("END_OF_INPUT", true);;
    public static final Symbol EPSILON = new Symbol("EPSILON", true);
}
