package com.analyzers;
import java.util.ArrayList;
import com.utils.*;
public class Lexical {
    String lexem = "";
    char previous;
    //lexType and currentType represents the current type of lexem or char stored.
    //-2 = comment error, -1 = invalid, 0 = none, 1 = Identifier, 2 = Number, 3 = Special, 4 = comment, 5 whitespace
    int lexType = 0;
    boolean complexSpecial = false;
    boolean commentEnded = false;

    public void analyze(String line, TokenTable tokTable, ArrayList<Symbol> symTable) throws Exception{
        char character;
        int currentType;
        for(int i = 0;i < line.length();i++){
            character = line.charAt(i);
            if(lexType == 0){
                //Ignorar caracter o verificar si va a terminar
            }else{
                
            }
        }
    }

    int charChecker(char character){
        //Check if number
        if(character >= 48 && character <= 57){
            return 2;
        //Check if whitespace, tab or new line
        }else if(character == 32 || character == 9 || character == 10){
            return 5;
        //Check if letter
        }else if((character >= 65 && character <= 90) || (character >= 97 && character <= 122)){
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
                        previous = 10;
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
                        previous = 10;
                        return 0;
                    }
                    return 3;
                case '*':
                    //Checks if is an open comment
                    if(previous == '/'){
                        return 4;
                    }
                    return 3;
                default:
                    //Return -1 for invalid character error.
                    return -1;
            }
        }
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
