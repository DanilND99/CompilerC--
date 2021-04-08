package com.utils;

public class Symbol {
    private int position = 0;
    private String type = "";
    /*
    An object to be stored in the symbol table,
    it contains type which is the type of lexem and can be:
    keyword, identifier, number or special.
    position refers to the key in the token table that stores the lexem.
    */
    //A constructor which creates a new symbol with the specified values.
    public Symbol(int position, String type){
        this.position = position;
        this.type = type;
    }
    //Returns the position value.
    public int getPosition(){
        return position;
    }
    //Sets the position value.
    public void setPosition(int position){
        this.position = position;
        return;
    }
    //Gets the type of symbol.
    public String getType(){
        return type;
    }
    //Sets the type of symbol.
    public void setType(String type){
        this.type = type;
        return;
    }
}
