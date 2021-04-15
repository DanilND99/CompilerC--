package com.utils;
import java.util.Map.Entry;
import java.util.HashMap;
public class Table {
    private int index = 1;
    String name ="";
    HashMap<Integer,String> table = new HashMap<Integer, String>();
    
    //Sets the name of the table on creation.
    public Table(String name){
        this.name = name;
    }

    /*
    FOR SYMBOL TABLE USE ONLY
    Recieves a lexem and returns its entry in the Symbol Table,
    if the lexem doesn't exist, it adds it then return its entry.
    */
    public int getAndSetSymbol(String lexem){
        if(!find(lexem)){
            set(lexem);
        }
        return getKey(lexem);
    }

    /*
    FOR TOKEN TABLE USE ONLY
    Recieves a lexem and returns its entry in the Token Table,
    if the lexem doesn't exist, it adds it then return its entry.
    */
    public int getAndSetToken(String lexem, int type){
        switch(type){
            case 1:
                if(keywordChecker(lexem)){
                    if(!find(lexem)){
                        set(lexem);
                    }
                    return getKey(lexem);
                }else{
                    if(!find("identifier")){
                        set("identifier");
                    }
                    return getKey("identifier");
                }
            case 2:
                if(!find("number")){
                    set("number");
                }
                return getKey("number");
            case 3:
                if(!find(lexem)){
                    set(lexem);
                }
                return getKey(lexem);
            default:
                return 0;
        }
    }
    //Checks if the lexem is a keyword
    public boolean keywordChecker(String lexem){
        switch(lexem){
            case "if":
            case "else":
            case "while":
            case "int":
            case "input":
            case "output":
            case "void":
            case "return":
            return true;
            default:
            return false;
        }
    }

    //Print the table.
    public void printTable(){
        System.out.println("------" + name + " Table------");
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
