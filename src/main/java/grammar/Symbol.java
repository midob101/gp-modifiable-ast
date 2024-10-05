package grammar;

public record Symbol(String name, boolean isTerminal) {
    public static Symbol EPSILON = new Symbol("EPSILON", true);
}
