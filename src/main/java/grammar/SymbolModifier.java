package grammar;

import java.util.Objects;

public class SymbolModifier {
    private String modifier;

    public SymbolModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifier() {
        return modifier;
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
