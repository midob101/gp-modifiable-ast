package parser.lr1_parser;

import language_definitions.PredefinedLanguages;
import lexer.exceptions.LexerParseException;
import main.GpModifiableAST;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import selectors.data.AliasSelector;
import selectors.data.ProductionSelector;
import selectors.data.TokenSelector;
import selectors.data.TokenValueSelector;
import selectors.logical.AndSelector;
import selectors.structural.HasImmediateChildSelector;
import syntax_tree.TreePrettyPrinter;
import syntax_tree.ast.AbstractSyntaxTreeNode;
import syntax_tree.ast.QueryResult;
import syntax_tree.ast.StringTreeNode;
import syntax_tree.ast.TokenTreeNode;
import syntax_tree.ast.exceptions.ReplacingUnconnectedNode;
import test_utils.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProofOfConceptTest {
    private static GpModifiableAST gpModifiableAST;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        gpModifiableAST = new GpModifiableAST();
        gpModifiableAST.load(PredefinedLanguages.MINIJAVA);
    }

    @Test
    public void testProof() throws IOException, LexerParseException, ReplacingUnconnectedNode {
        AbstractSyntaxTreeNode ast = gpModifiableAST.createAst(new File("src/test/java/parser/lr1_parser/test_languages/minijava/proof_of_concept.minijava"));
        System.out.println(TreePrettyPrinter.print(ast));

        QueryResult messageSendEntries = ast.query(
                new AndSelector(
                    new ProductionSelector("MESSAGE_SEND"),
                    new HasImmediateChildSelector(
                            new AndSelector(
                                    new AliasSelector("functionName"),
                                    new TokenValueSelector("translate")
                            )
                    )
                )
        );

        for(AbstractSyntaxTreeNode node: messageSendEntries) {
            QueryResult passedValue = node.queryImmediateChildren(new ProductionSelector("EXPRESSION_LIST"))
                    .queryImmediateChildren(new ProductionSelector("EXPRESSION"))
                    .queryImmediateChildren(new TokenSelector("integer_literal"));

            TokenTreeNode param = (TokenTreeNode) passedValue.getResult().get(0);
            if(param.getToken().getValue().equals("1") || param.getToken().getValue().equals("3")) {
                node.replace(
                        new StringTreeNode("NewTranslate.translate("+param.getToken().getValue()+", true)")
                );
            } else {
                node.replace(
                        new StringTreeNode("NewTranslate.translate("+param.getToken().getValue()+", false)")
                );
            }
        }


        String expected = Files.readString(Path.of("src/test/java/parser/lr1_parser/test_data/minijava/proof_of_concept.minijava"), StandardCharsets.UTF_8);
        Assertions.assertEquals(
                StringUtilities.trimEmptyLines(StringUtilities.useCRLF(expected)),
                StringUtilities.trimEmptyLines(StringUtilities.useCRLF(ast.getSourceCode()))
        );
    }
}
