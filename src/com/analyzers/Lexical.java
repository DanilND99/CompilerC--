package com.analyzers;
import java.util.ArrayList;
import com.utils.*;
import com.exceptions.*;
public class Lexical {
    String lexem = "";
    char previous;
    /*
    Lexical is the implementation of the lexical analyzer, it contains two functions: analyze and charChecker.
    Its role is to fill the token and symbol table apropiately.
    lexType and currentType represents the current type of lexem or char stored.
    Errors when current type is:
    -1 = invalid character, -2 = comment error, -3 = invalid number, -4 = invalid identifier, -5 = invalid use of !
    The state of the lexem type and current type.
    0 = none, 1 = Identifier, 2 = Number, 3 = Special, 4 = comment, 5 whitespace
    */
    int lexType = 0;
    boolean complexSpecial = false;
    boolean commentEnded = false;
    boolean commentBegin = false;
    /*
    analyze recieves a line of the file and the tables to determinate and store all the symbols in the line,
    it can also maintain a comment state through multiple calls if needed.
    */
    public void analyze(String line, Table tokTable, Table idTable, Table numTable, ArrayList<Token> stream) throws Exception{
        char character;
        int currentType;
        //Iterates through the characters in the line.
        for(int i = 0;i < line.length();i++){
            character = line.charAt(i);
            int tokenEntry = 0;
            int symbolEntry = 0;
            //Set the type of the current character.
            currentType = charChecker(character);
            //if an error is returned, throws back the error type.
            if(currentType < 0){
                switch(currentType){
                    case -1:
                        throw new InvalidCharacterException();
                    case -2:
                        throw new InvalidCloseCommentException();
                    case -3:   
                        throw new InvalidNumberException();
                    case -4:
                        throw new InvalidIdentifierException();
                    case -5:
                        throw new InvalidExclamationException();
                }
                return;
            }
            //Checks the type of the previous character and determinates the actions to do.
            switch(lexType){
                //If there is no character behind or the previous character is a whitespace.
                case 0:
                    //and the current character is not a whitespace, sets the new lexType and concatenate the character to the lexem.
                    if(currentType != 5){
                        lexType = currentType;
                        lexem = lexem.concat("" + character);
                    }
                    previous = character;
                    break;
                //If the previous character is a letter or a number.
                case 1:
                case 2:
                    //And the current character is of the same type.
                    if(currentType == 1 || currentType == 2){
                        //Concatenate the character to the lexem.
                        lexem = lexem.concat("" + character);
                        previous = character;
                        break;
                    }
                    // Set the lexem in the token table and return its entry.
                    tokenEntry = tokTable.getAndSetToken(lexem, lexType);
                    // If the lexem was a number or identifier, adds it to the corresponding symbol table.
                    if(!tokTable.find(lexem)){
                        if(lexType == 1){
                            symbolEntry = idTable.getAndSetSymbol(lexem);
                        }else if(lexType == 2){
                            symbolEntry = numTable.getAndSetSymbol(lexem);
                        }
                    }else{
                        //In the case there is an identifier "identifier" or "number", add it to the idTable.
                        if(lexem.equals("identifier") || lexem.equals("number")){
                            symbolEntry = idTable.getAndSetSymbol(lexem);
                        }
                    }
                    //Create and add the token to the stream.
                    stream.add(new Token(tokenEntry, symbolEntry));
                    previous = character;
                    //If the current character is a special symbol, then clean the lexem and add it.
                    if(currentType == 3){
                        lexem = "" + character;
                        lexType = currentType;
                    //If is a whitespace, clean the lexem and set the previous type to none.
                    }else if(currentType == 5){
                        lexem = "";
                        lexType = 0;
                    }
                    break;
                //If the previous character is a special character.
                case 3:
                    //Check if there was a comment initialized.
                    if(commentBegin){
                        /*
                        Clean the lexem, sets the previous character to whitespace,
                        set the previous character type to comment and unflags the beginning of a comment.
                        */
                        previous = ' ';
                        lexType = currentType;
                        lexem = "";
                        commentBegin = false;
                        break;
                    }
                    //If is detected that the two characters formed a complex character (==, <=, >=, !=).
                    if(currentType == 3 && complexSpecial){
                        /*
                        Concatenate the current character into the lexem,
                        unflag the complex character and set the current type as whitespace.
                        This so the lexem can be cleaned a little bit later.
                        */
                        lexem = lexem.concat("" + character);
                        complexSpecial = false;
                        currentType = 5;
                    }
                    //Add the previous lexem into the token table.
                    tokenEntry = tokTable.getAndSetToken(lexem, lexType);
                    //Add the token into the stream.
                    stream.add(new Token(tokenEntry, symbolEntry));
                    previous = character;
                    //If the current character is a whitespace, clean the lexem and set type to none.
                    if(currentType == 5){
                        lexem = "";
                        lexType = 0;
                    //If not, clean the lexem and concatenate the new character.
                    }else{
                        lexem = "" + character;
                        lexType = currentType;
                    }
                    break;
                //If we are in a comment.
                case 4:
                    //Check if the comment has ended.
                    if(commentEnded){
                        //Set the lexem type to none, set the current character to whitespace and unflag the end of the comment.
                        lexType = 0;
                        character = ' ';
                        commentEnded = false;
                    }
                    //Just set the new previous character to be able to find the "*/" when it appears.
                    previous = character;
                    break;
            }
        }
        /*
        When the iteration is over, we need to check the state of the lexem.
        Because this is the same as recieving a whitespace.
        If the lexem is empty, do nothing.
        */
        if(!lexem.equals("")){
            //If the lexem is "!" then throw the xxclamation exception.
            if(lexem.equals("!")){
                throw new InvalidExclamationException();
            }
            //Add the lexem into the token table.
            int tokenEntry = tokTable.getAndSetToken(lexem, lexType);
            int symbolEntry = 0;
            //Add the lexem to the symbol table according to its type.
            switch(lexType){
                case 1:
                    if(!tokTable.find(lexem)){
                        symbolEntry = idTable.getAndSetSymbol(lexem);
                    }else{
                        //In the case there is an identifier "identifier" or "number", add it to the idTable.
                        if(lexem.equals("identifier") || lexem.equals("number")){
                            symbolEntry = idTable.getAndSetSymbol(lexem);
                        }
                    }
                    break;
                case 2:
                    symbolEntry = numTable.getAndSetSymbol(lexem);
                    break;
            }
            //Add the token to the stream.
            stream.add(new Token(tokenEntry, symbolEntry));
            //Clean the lexem, set previous to whitespace, and set the lexem type to none.
            lexType = 0;
            lexem = "";
            previous = ' ';
        }
    }
    /*
    charChecker recieves the current character being analyzed and returns
    its determined type of character, it can also identies the error types
    and return them.
    */
    public int charChecker(char character){
        //Check if there is a miss use of "!".
        if(previous == '!' && character != '='){
            //Ignores the error if the analyzer is in a comment by treating it like a whitespace.
            if (lexType == 4){
                return 5;
            }
            return -5;
        }
        //Check if number.
        if(character >= 48 && character <= 57){
            //Check if there is a "a1" type of identifier and returns -4 if it is.
            if((previous >= 65 && previous <= 90) || (previous >= 97 && previous <= 122) && lexType != 4){
                return -4;
            }
            return 2;
        //Check if whitespace, tab or new line.
        }else if(character == 32 || character == 9 || character == 10){
            return 5;
        //Check if letter.
        }else if((character >= 65 && character <= 90) || (character >= 97 && character <= 122)){
            //Check if there is a "1a" type of number and returns -3 if it is.
            if(previous >= 48 && previous <= 57 && lexType != 4){
                return -3;
            }
            return 1;
        }else{
            switch(character){
                //Check for special characters.
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
                //Checks if = is part of a complex character.
                case '=':
                    if(previous == '!' || previous == '<' || previous == '>' || previous == '='){
                        complexSpecial = true;
                    }
                    return 3;
                case '/':
                    //Checks if is a close comment.
                    if(previous == '*'){
                        //Checks if the close comment is in a comment, if not, returns -2.
                        if(lexType != 4){
                            return -2;
                        }
                        //Sets the flag of the end of a commment.
                        commentEnded = true;
                        return 0;
                    }
                    return 3;
                case '*':
                    //Checks if is an open comment.
                    if(previous == '/'){
                        //Sets the flag of the beginning of a comment.
                        commentBegin = true;
                        return 4;
                    }
                    return 3;
                default:
                    //If the invalid character is in a comment, ignores it and returns a whitespace.
                    if(lexType == 4){
                        return 5;
                    }
                    //Return -1 for invalid character error.
                    return -1;
            }
        }
    }
    //Gets the type of the last character analyzed by the lexical analyzer.
    public int getLexType(){
        return lexType;
    }
}
