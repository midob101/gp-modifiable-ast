package lexer_post_process;

import lexer.TokenList;

public interface ILexerPostProcessor {
    public void postProcess(TokenList tokenList);
}
