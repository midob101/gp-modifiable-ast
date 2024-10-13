package ConfigReader;

import config_reader.ConfigReader;
import config_reader.NameAndModifiers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigReaderTest {

    @Test
    public void testSplitNameWithModifiers() {
        NameAndModifiers nameAndModifiers = ConfigReader.splitNameWithModifiers("  PRODUCTION_NAME   [hidden, alias = 192, alias=produ, foobar  ]");

        Assertions.assertEquals("PRODUCTION_NAME", nameAndModifiers.name());
        Assertions.assertEquals(4, nameAndModifiers.modifiers().size());
        Assertions.assertEquals("hidden", nameAndModifiers.modifiers().get(0).getModifier());
        Assertions.assertEquals("alias", nameAndModifiers.modifiers().get(1).getModifier());
        Assertions.assertEquals("192", nameAndModifiers.modifiers().get(1).getValue());
        Assertions.assertEquals("alias", nameAndModifiers.modifiers().get(2).getModifier());
        Assertions.assertEquals("produ", nameAndModifiers.modifiers().get(2).getValue());
        Assertions.assertEquals("foobar", nameAndModifiers.modifiers().get(3).getModifier());
    }

    @Test
    public void testSplitNameWithoutModifiers() {
        NameAndModifiers nameAndModifiers = ConfigReader.splitNameWithModifiers("PRODUCTION_NAME");

        Assertions.assertEquals("PRODUCTION_NAME", nameAndModifiers.name());
        Assertions.assertEquals(0, nameAndModifiers.modifiers().size());
    }
}
