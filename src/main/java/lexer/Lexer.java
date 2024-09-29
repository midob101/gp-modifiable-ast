package lexer;

import lexer.custom_matchers.SingleLineCommentMatcher;
import lexer.exceptions.LexerParseException;
import logger.Logger;
import logger.LoggerComponents;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class for the lexer process.
 *
 * This file gets called with a filename and returns the list of tokens.
 * It can also reverse the process to create from a list of tokens a file.
 */
public class Lexer {
    public TokenList runForFile(String fileName, LexerDefinitionList lexerDefinitionList) throws IOException, LexerParseException {
        LexerContext context = new LexerContext(new File(fileName), lexerDefinitionList);
        runForContext(context);
        System.out.println(SingleLineCommentMatcher.class);
        return context.getTokenList();
    }

    public String backToSource(TokenList tokenList) {
        return tokenList.backToSource();
    }

    private void runForContext(LexerContext context) throws LexerParseException {
        while (!context.isDone()) {
            addNextToken(context);
        };
    }

    private void addNextToken(LexerContext context) throws LexerParseException {
        if(Logger.LOG_LEVEL == Logger.LOG_LEVELS.DEBUG) {
            int lineNumber = context.getCurrentLineNumber();
            int columnNumber = context.getCurrentColumnNumberOfLine();
            Logger.debug(LoggerComponents.LEXER, "Starting to parse next token, current line " + lineNumber + ", column " + columnNumber);
        }



        LexerDefinition match = null;
        int matchLength = -1;

        /**
         * TODO: Think about running through the definition list from back first, exit on first match
         */
        for (LexerDefinition lexerDefinition: context.getLexerDefinitionList().getDefinitionList()) {
            LexerDefinition newMatch = null;

            String matchedString = getNextTokenByLiteral(context, lexerDefinition);
            if(matchedString == null) {
                matchedString = getNextTokenByRegex(context, lexerDefinition);
            }
            if(matchedString == null) {
                matchedString = getNextTokenByCustomMatcher(context, lexerDefinition);
            }

            if(matchedString != null) {
                newMatch = lexerDefinition;
                matchLength = matchedString.length();
            }

            if(match != null && newMatch != null) {
                Logger.debug(LoggerComponents.LEXER, "Overwriting old match. Previous match: " + match.getName() + ", New match: " + newMatch.getName());
            }

            if(newMatch != null) {
                match = newMatch;
            }
        }

        if(match == null) {
            int lineNumber = context.getCurrentLineNumber();
            int columnNumber = context.getCurrentColumnNumberOfLine();
            throw new LexerParseException("Failed to parse file. Line " + lineNumber + ", Column " + columnNumber);
        } else {
            Token t = new Token(match, context.getNextNChars(matchLength));
            context.addToken(t);
            context.moveParseIndex(matchLength);
        }
    }

    private String getNextTokenByLiteral(LexerContext context, LexerDefinition lexerDefinition) {
        if(!lexerDefinition.getLiteral().isEmpty()) {
            int literalSize = lexerDefinition.getLiteral().length();
            if (context.getNextNChars(literalSize).equals(lexerDefinition.getLiteral())) {
                return lexerDefinition.getLiteral();
            }
        }
        return null;
    }

    private String getNextTokenByRegex(LexerContext context, LexerDefinition lexerDefinition) {
        if(!lexerDefinition.getPattern().isEmpty()) {
            String remaining = context.getRemainingSource();
            String regex = "^(" + lexerDefinition.getPattern() + ")";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(remaining);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return null;
    }

    private String getNextTokenByCustomMatcher(LexerContext context, LexerDefinition lexerDefinition) {
        if(lexerDefinition.getCustomMatcher() != null) {
            return lexerDefinition.getCustomMatcher().match(context);
        }
        return null;
    }
}
