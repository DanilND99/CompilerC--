package com.utils;

public class Token {
    private int tokenEntry = 0;
    private int symbolEntry = 0;
    private int line = 0;
    /*
    An object to be stored in the token stream,
    it contains tokenEntry which refers to the key in the token table,
    symbolEntry refers to the key in the symbol tables.
    */
    //A constructor which creates a new token with the specified values.
    public Token(int token, int symbol){
        tokenEntry = token;
        symbolEntry = symbol;
    }

    //A constructor which creates a new token with the specified values.
    public Token(int token, int symbol, int lineNumber){
        tokenEntry = token;
        symbolEntry = symbol;
        line = lineNumber;
    }

    //Returns the tokenEntry value.
    public int getTokenEntry(){
        return tokenEntry;
    }
    //Sets the tokenEntry value.
    public void setTokenEntry(int entry){
        tokenEntry = entry;
        return;
    }
    //Returns the symbolEntry value.
    public int getSymbolEntry(){
        return symbolEntry;
    }
    //Sets the tokenEntry value.
    public void setSymbolEntry(int entry){
        symbolEntry = entry;
        return;
    }

    //Gets the line.
    public int getLine(){
        return line;
    }
    //Sets the line.
    public void setLine(int x){
        line = x;
        return;
    }
}
