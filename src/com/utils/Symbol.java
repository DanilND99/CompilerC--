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
    public Symbol(int position, String type){
        this.position = position;
        this.type = type;
    }

    public int getPosition(){
        return position;
    }
    public void setPosition(int position){
        this.position = position;
        return;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
        return;
    }
}
