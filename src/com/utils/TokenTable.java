package com.utils;

import java.util.Map.Entry;
import java.util.HashMap;

public class TokenTable {
    private int index = 1;
    HashMap<Integer,String> table = new HashMap<Integer, String>();
    /*
    An object that contains a HashMap representing the token table
    and an index to increment the key of the Map with every new entry.
    */
    //Default initialization of the token table.
    public TokenTable(){
        table.put(index, "if");
        index++;
        table.put(index, "else");
        index++;
        table.put(index, "int");
        index++;
        table.put(index, "while");
        index++;
        table.put(index, "input");
        index++;
        table.put(index, "output");
        index++;
        table.put(index, "void");
        index++;
        table.put(index, "return");
        index++;
        table.put(index, "+");
        index++;
        table.put(index, "-");
        index++;
        table.put(index, "*");
        index++;
        table.put(index, "/");
        index++;
        table.put(index, "<");
        index++;
        table.put(index, ">");
        index++;
        table.put(index, "<=");
        index++;
        table.put(index, ">=");
        index++;
        table.put(index, "==");
        index++;
        table.put(index, "!=");
        index++;
        table.put(index, "=");
        index++;
        table.put(index, ";");
        index++;
        table.put(index, ",");
        index++;
        table.put(index, "(");
        index++;
        table.put(index, ")");
        index++;
        table.put(index, "[");
        index++;
        table.put(index, "]");
        index++;
        table.put(index, "{");
        index++;
        table.put(index, "}");
        index++;
    }
    //Print the token table.
    public void printTable(){
        System.out.println("------Token table------");
        for(int i = 1; i < index;i++){
            System.out.println("Entry: "+ i + "   Value: " + table.get(i));
        }
        System.out.println("");
        return;
    }

    //Get the value of the inputed key or table position.
    public String getValue(int key){
        return table.get(key);
    }

    //Set a new entry on the table.
    public void set(String value){
        table.put(index, value);
        index++;
        return;
    }

    //Check if a value is on the table.
    public boolean find(String value){
        return table.containsValue(value);
    }

    //Gets the corresponding key to a specified value, returns -1 if the value is not found.
    public int getKey(String value){
        for(Entry<Integer, String> entry: table.entrySet()) {
            if(entry.getValue().equals(value)) {
              return entry.getKey();
            }
        }
        return -1;
    }
}
