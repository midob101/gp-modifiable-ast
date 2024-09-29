package lexer.post_process;

import lexer.LexerContext;

public interface ILexerPostProcessor {
    void run(LexerContext context);
}
