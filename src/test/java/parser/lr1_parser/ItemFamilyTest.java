package parser.lr1_parser;

import config_reader.ConfigReader;
import language_definitions.LanguageDefinition;
import org.junit.Assert;
import org.junit.Test;
import parser.lr1_parser.items.ItemFamily;
import parser.lr1_parser.items.PrettyPrintItemFamily;
import test_utils.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ItemFamilyTest {

    /**
     * Creates all item sets and puts them into an item family.
     *
     * Test case is based of the example on pages 7-8 of
     * https://web.stanford.edu/class/archive/cs/cs143/cs143.1128/handouts/110%20LR%20and%20SLR%20Parsing.pdf
     * which is based on pp. 231-236 Aho/Sethi/Ullman (Compilers: Principles, Techniques, and Tools)
     */
    @Test
    public void testItemFammilyCreation() throws IOException {
        LanguageDefinition languageDefinition = ConfigReader.read(new File("src/test/java/parser/lr1_parser/test_languages/aabaaab.txt"));
        ItemFamily itemFamily = new ItemFamily();
        itemFamily.create(languageDefinition);
        String expected = Files.readString(Path.of("src/test/java/parser/lr1_parser/test_data/ItemFamilyTest.txt"), StandardCharsets.UTF_8);
        Assert.assertEquals(
                StringUtilities.trimEmptyLines(StringUtilities.useCRLF(expected)),
                StringUtilities.trimEmptyLines(StringUtilities.useCRLF(PrettyPrintItemFamily.getItemFamilyString(itemFamily)))
        );
    }
}
