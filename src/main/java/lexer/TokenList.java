package lexer;

import java.util.LinkedList;

public class TokenList {
    private final LinkedList<Token> tokenList = new LinkedList<>();

    public LinkedList<Token> getIterable() {
        return tokenList;
    }

    public void addToken(Token token) {
        tokenList.add(token);
    }

    public String backToSource() {
        StringBuilder result = new StringBuilder();
        for (Token token : tokenList) {
            result.append(token.getValue());
        }
        return result.toString();
    }

    /**
     * Only use this method for test output purposes.
     * This is not supposed to be used for anything else.
     *
     * If you want to revert back to source code, use the backToSource method.
     */
    @Override
    public String toString() {
        StringBuilder stringified = new StringBuilder();
        for (Token token : tokenList) {
            stringified.append(token.toString()).append("\r\n");
        }
        return stringified.toString();
    }
}
