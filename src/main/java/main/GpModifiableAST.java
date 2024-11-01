package main;

import config_reader.ConfigReader;
import config_reader.ConfigReaderException;
import language_definitions.LanguageDefinition;
import lexer.Lexer;
import lexer.TokenList;
import lexer.exceptions.LexerParseException;
import logger.Logger;
import logger.LoggerComponents;
import parser.lr1_parser.Parser;
import syntax_tree.AbstractSyntaxTreeFactory;
import syntax_tree.ConcreteSyntaxTreeNode;
import syntax_tree.ast.AbstractSyntaxTreeNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GpModifiableAST {
    private LanguageDefinition languageDefinition = null;
    private Parser parser = null;

    /**
     * Generates all necessary informations required to parse a specific language.
     * This function can be expensive for large language files. This should only called once for each language file.
     *
     * @param languageFile The file that should be used.
     * @throws ConfigReaderException
     */
    public void load(File languageFile) throws ConfigReaderException {
        languageDefinition = ConfigReader.read(languageFile);
        parser = new Parser();
        parser.initialize(languageDefinition);
    }

    /**
     * Creates the AST for a given file.
     *
     * @param input The file to be parsed.
     * @return The root node of the AST
     * @throws LexerParseException
     * @throws IOException
     */
    public AbstractSyntaxTreeNode createAst(File input) throws LexerParseException, IOException {
        if(parser == null) {
            Logger.err(LoggerComponents.MAIN,"The language definition does not exist, please call load before.");
            throw new RuntimeException("The language definition does not exist, please call load before.");
        }

        Lexer lexer = new Lexer();
        TokenList tokenList = lexer.runForFile(input, languageDefinition);
        ConcreteSyntaxTreeNode cst = parser.createCST(tokenList);
        return AbstractSyntaxTreeFactory.create(cst);
    }

    /**
     * Saves the AST node to a given output file.
     *
     * The entire file contents are replaced with the contents of the passed node. If not called on the root node,
     * this only takes the contents of the subtree.
     *
     * @param output Target file
     * @param node The ast node to be written
     * @throws IOException
     */
    public void saveToFile(File output, AbstractSyntaxTreeNode node) throws IOException {
        if(node.getParent() != null) {
            Logger.warn(LoggerComponents.MAIN,"The node to be saved has a parent node. This might cause unwanted removed code.");
        }

        FileWriter writer = new FileWriter(output);
        writer.write(node.getSourceCode());
        writer.close();
    }
}
