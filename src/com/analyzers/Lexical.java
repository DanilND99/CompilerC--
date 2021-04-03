package com.analyzers;
import java.util.ArrayList;
import com.utils.*;
public class Lexical {
    String lexem = "";
    char previous;
    //lexType and currentType represents the current type of lexem or char stored.
    //-5 = invalid use of !, -4 = invalid identifier, -3 = invalid number, -2 = comment error, -1 = invalid character
    //0 = none, 1 = Identifier, 2 = Number, 3 = Special, 4 = comment, 5 whitespace
    int lexType = 0;
    boolean complexSpecial = false;
    boolean commentEnded = false;
    boolean commentBegin = false;

    public void analyze(String line, TokenTable tokTable, ArrayList<Symbol> symTable) throws Exception{
        char character;
        int currentType;
        for(int i = 0;i < line.length();i++){
            character = line.charAt(i);
            currentType = charChecker(character);
            if(currentType < 0){
                System.out.println("Error");
                //Implement exceptions here
                return;
            }
            switch(lexType){
                case 0:
                    if(currentType != 5){
                        lexType = currentType;
                        lexem = lexem.concat("" + character);
                    }
                    previous = character;
                    break;
                case 1:
                case 2:
                    if(currentType == 1 || currentType == 2){
                        lexem = lexem.concat("" + character);
                        previous = character;
                        break;
                        //Check if multiple identifier with a letter different
                    }
                    boolean exist = tokTable.find(lexem);
                    if(!exist){
                       tokTable.set(lexem); 
                    }
                    int position = tokTable.getKey(lexem);
                    if(position < 9 && position > 0){
                        symTable.add(new Symbol(position,"keyword"));
                    }else if(lexType == 1){
                        symTable.add(new Symbol(position,"identifier"));
                    }else if(lexType == 2){
                        symTable.add(new Symbol(position,"number"));
                    }
                    previous = character;
                    if(currentType == 3){
                        lexem = "" + character;
                        lexType = currentType;
                    }else if(currentType == 5){
                        lexem = "";
                        lexType = 0;
                    }
                    break;
                case 3:
                    if(commentBegin){
                        previous = ' ';
                        lexType = currentType;
                        lexem = "";
                        commentBegin = false;
                        break;
                    }
                    if(currentType == 3 && complexSpecial){
                        lexem = lexem.concat("" + character);
                        complexSpecial = false;
                        currentType = 5;
                    }
                    symTable.add(new Symbol(tokTable.getKey(lexem),"special"));
                    previous = character;
                    if(currentType == 5){
                        lexem = "";
                        lexType = 0;
                    }else{
                        lexem = "" + character;
                        lexType = currentType;
                    }
                    break;
                case 4:
                    if(commentEnded){
                        lexType = 0;
                        character = ' ';
                        commentEnded = false;
                    }
                    previous = character;
                    break;
            }
        }
        
        if(!lexem.equals("")){
            boolean exist = tokTable.find(lexem);
            if(!exist){
                tokTable.set(lexem); 
            }
            int position = tokTable.getKey(lexem);
            switch(lexType){
                case 1:
                    if(position < 9 && position > 0){
                        symTable.add(new Symbol(position,"keyword"));
                    }else{
                        symTable.add(new Symbol(position,"identifier"));
                    }
                    break;
                case 2:
                    symTable.add(new Symbol(position,"number"));
                    break;
                case 3:
                    symTable.add(new Symbol(position,"special"));
                    break;
            }
            lexType = 0;
            lexem = "";
            previous = ' ';
        }
    }

    

    public int charChecker(char character){
        //Check if there is a miss use of "!".
        if(previous == '!' && character != '='){
            return -5;
        }
        //Check if number
        if(character >= 48 && character <= 57){
            //Check if there is a "a1" type of identifier and returns -4 if it is.
            if((previous >= 65 && previous <= 90) || (previous >= 97 && previous <= 122)){
                return -4;
            }
            return 2;
        //Check if whitespace, tab or new line
        }else if(character == 32 || character == 9 || character == 10){
            return 5;
        //Check if letter
        }else if((character >= 65 && character <= 90) || (character >= 97 && character <= 122)){
            //Check if there is a "1a" type of number and returns -3 if it is.
            if(previous >= 48 && previous <= 57){
                return -3;
            }
            return 1;
        }else{
            switch(character){
                //Check for special characters
                case '+':
                case '-':
                case '(':
                case ')':
                case '[':
                case ']':
                case '{':
                case '}':
                case ',':
                case ';':
                case '!':
                case '<':
                case '>':
                    return 3;
                //Checks if = is part of a complex character
                case '=':
                    if(previous == '!' || previous == '<' || previous == '>' || previous == '='){
                        complexSpecial = true;
                    }
                    return 3;
                case '/':
                    //Checks if is a close comment
                    if(previous == '*'){
                        //Checks if the close comment is in a comment
                        if(lexType != 4){
                            return -2;
                        }
                        commentEnded = true;
                        return 0;
                    }
                    return 3;
                case '*':
                    //Checks if is an open comment
                    if(previous == '/'){
                        commentBegin = true;
                        return 4;
                    }
                    return 3;
                default:
                    //Return -1 for invalid character error.
                    return -1;
            }
        }
    }

    public void setPrevious(char chr){
        previous = chr;
    }
    public void iterate(String x){
        char character;
        System.out.println(x);
        for(int i = 0;i < x.length();i++){
            character = x.charAt(i);
            if(character >= 48 && character <= 57){
                System.out.println("number");
            }else if(character == 32 || character == 9 || character == 10){
                System.out.println("whitespace");
            }else if((character >= 65 && character <= 90) || (character >= 97 && character <= 122)){
                System.out.println("letter");
            }else{
                switch(character){
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '=':
                    case '<':
                    case '>':
                    case '(':
                    case ')':
                    case '[':
                    case ']':
                    case '{':
                    case '}':
                    case '!':
                    case ',':
                    case ';':
                        System.out.println("Special character");
                        break;
                    default:
                        System.out.println(character);
                }
                //System.out.println(character);
            }
        }
        System.out.println("\tend\n");
    }

    public void test(TokenTable tokTable, ArrayList<Symbol> symTable){
        tokTable.set("test");
        symTable.add(new Symbol(90,"Test"));
    }
}
