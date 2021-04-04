package com.exceptions;

public class InvalidCharacterException extends Exception {
    
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     */
    private static final long serialVersionUID = 1L;
    public InvalidCharacterException(){
        super("Invalid character detected in source code");
    }
    public InvalidCharacterException(String message){
        super(message);
    }
}
