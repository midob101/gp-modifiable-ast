package grammar;

import java.util.Objects;

public class SymbolModifier {
    public final static String HIDDEN = "hidden";
    public final static String ALIAS = "alias";
    public final static String LIST = "list";

    private final String value;
    private final String modifier;

    public SymbolModifier(String modifier) {
        this.modifier = modifier;
        this.value = null;
    }

    public SymbolModifier(String modifier, String value) {
        this.modifier = modifier;
        this.value = value;
    }

    public String getModifier() {
        return modifier;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SymbolModifier that)) return false;
        return Objects.equals(modifier, that.modifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(modifier);
    }
}
