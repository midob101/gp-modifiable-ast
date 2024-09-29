package lexer;

import java.util.LinkedList;

public class LexerDefinitionList {
    private final LinkedList<LexerDefinition> tokens = new LinkedList<>();

    public void addToken(LexerDefinition token) {
        tokens.add(token);
    }

    public LinkedList<LexerDefinition> getDefinitionList() {
        return tokens;
    }
}
