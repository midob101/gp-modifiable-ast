package language_definitions;

import grammar.Production;
import grammar.Symbol;
import lexer.LexerDefinition;
import logger.Logger;
import logger.LoggerComponents;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This file contains the configuration of a language
 * The values are injected by the json parser.
 */
public class LanguageDefinition {
    private String languageName = "undefined";
    private boolean singleLineCommentAvailable = false;
    private String singleLineCommentStyle;
    private boolean multiLineCommentAvailable = false;
    private String multiLineCommentStyleStart;
    private String multilineCommentStyleEnd;
    private boolean caseSensitive = true;
    private final List<LexerDefinition> lexerDefinitionList = new LinkedList<>();
    private final List<Production> productions = new LinkedList<>();
    private Symbol grammarStartSymbol;

    public String getLanguageName() {
        return languageName;
    }

    public boolean isSingleLineCommentAvailable() {
        return singleLineCommentAvailable;
    }

    public String getSingleLineCommentStyle() {
        return singleLineCommentStyle;
    }

    public boolean isMultiLineCommentAvailable() {
        return multiLineCommentAvailable;
    }

    public String getMultiLineCommentStyleStart() {
        return multiLineCommentStyleStart;
    }

    public String getMultilineCommentStyleEnd() {
        return multilineCommentStyleEnd;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setSingleLineCommentAvailable(boolean singleLineCommentAvailable) {
        this.singleLineCommentAvailable = singleLineCommentAvailable;
    }

    public void setSingleLineCommentStyle(String singleLineCommentStyle) {
        this.singleLineCommentStyle = singleLineCommentStyle;
    }

    public void setMultiLineCommentAvailable(boolean multiLineCommentAvailable) {
        this.multiLineCommentAvailable = multiLineCommentAvailable;
    }

    public void setMultiLineCommentStyleStart(String multiLineCommentStyleStart) {
        this.multiLineCommentStyleStart = multiLineCommentStyleStart;
    }

    public void setMultilineCommentStyleEnd(String multilineCommentStyleEnd) {
        this.multilineCommentStyleEnd = multilineCommentStyleEnd;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public void addLexerDefinition(LexerDefinition lexerDefinition) {
        lexerDefinitionList.add(lexerDefinition);
    }

    public List<LexerDefinition> getLexerDefinitionList() {
        return lexerDefinitionList;
    }

    public void addProduction(Production production) {
        this.productions.add(production);
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setGrammarStartSymbol(Symbol symbol) {
        this.grammarStartSymbol = symbol;
    }

    public Symbol getGrammarStartSymbol() {
        return this.grammarStartSymbol;
    }

    public Set<Symbol> getAllGrammarSymbols() {
        Set<Symbol> symbols = new LinkedHashSet<>();
        for(Production production : productions) {
            symbols.addAll(production.getSymbols());
            symbols.add(production.leftHandSymbol());
        }
        return symbols;
    }

    public Set<Symbol> getAllNonTerminalSymbols() {
        Set<Symbol> symbols = this.getAllGrammarSymbols();
        return symbols.stream().filter((symbol) -> !symbol.isTerminal()).collect(Collectors.toSet());
    }

    public boolean isValid() {
        boolean isValid = true;
        List<LexerDefinition> terminals = this.getLexerDefinitionList();
        List<Production> productions = this.getProductions();

        for(Symbol symbol : this.getAllGrammarSymbols()) {
            if(!symbol.isTerminal()) {
                if(productions.stream().filter((production) -> production.leftHandSymbol().equals(symbol)).findAny().isEmpty()) {
                    Logger.err(LoggerComponents.CONFIG_READER, "Missing definition of non terminal " + symbol.name());
                    isValid = false;
                }
            } else {
                if(terminals.stream().filter((terminal) -> terminal.getName().equals(symbol.name())).findAny().isEmpty()) {
                    Logger.err(LoggerComponents.CONFIG_READER, "Missing definition of terminal " + symbol.name());
                    isValid = false;
                }
            }
        }
        return isValid;
    }
}
