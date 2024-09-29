package lexer;

import java.util.LinkedList;

public class TokenList {
    private final LinkedList<Token> tokenList = new LinkedList<>();

    public LinkedList<Token> getTokenList() {
        return tokenList;
    }

    public void addToken(Token token) {
        tokenList.add(token);
    }

    public String backToSource() {
        StringBuilder result = new StringBuilder();
        for (Token token : tokenList) {
            result.append(token.value());
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return tokenList.toString();
    }
}
