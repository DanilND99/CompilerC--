package com.analyzers;
import java.util.ArrayList;
import com.utils.*;
public class Lexical {
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
