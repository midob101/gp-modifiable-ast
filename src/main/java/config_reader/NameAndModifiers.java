package config_reader;

import grammar.SymbolModifier;

import java.util.List;

public record NameAndModifiers(String name, List<SymbolModifier> modifiers) {
}
