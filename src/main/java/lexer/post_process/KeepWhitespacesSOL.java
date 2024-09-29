package lexer.post_process;

import lexer.LexerContext;
import lexer.Token;

/**
 * Keeps the whitespaces in the AST at the start of a line (SOL).
 * This postprocessor requires seperate tokens for whitespaces and newlines.
 * They have to be called "whitespace" and "newline" respectively.
 */
public class KeepWhitespacesSOL implements ILexerPostProcessor {

    @Override
    public void run(LexerContext context) {
        boolean isInNewLine = true;
        for (Token token : context.getTokenList().getIterable()) {
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
