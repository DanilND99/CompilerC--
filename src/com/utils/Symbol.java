package com.utils;

public class Symbol {
    private int position = 0;
    private String type = "";

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
