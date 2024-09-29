package lexer_post_process.predefined_post_processors;

import lexer.Token;
import lexer.TokenList;
import lexer_post_process.ILexerPostProcessor;

/**
 * Keeps the whitespaces in the AST at the start of a line.
 * This postprocessor requires seperate tokens for whitespaces and newlines.
 * They have to be called "whitespace" and "newline" respectively.
 */
public class KeepWhitespacesSOL implements ILexerPostProcessor {

    @Override
    public void postProcess(TokenList tokenList) {
        boolean isInNewLine = true;
        for (Token token : tokenList.getTokenList()) {
            if (token.getLexerDefinition().getName().equals("whitespace")) {
                token.setKeepInAST(isInNewLine);
            } else {
                isInNewLine = false;
            }

            if(token.getLexerDefinition().getName().equals("newline")) {
                isInNewLine = true;
            }
        }
    }
}
