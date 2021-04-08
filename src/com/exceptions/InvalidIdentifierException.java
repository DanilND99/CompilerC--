package com.exceptions;

public class InvalidIdentifierException extends Exception {
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     The special exception when an invalid identifier "a1" is detected.
     */
    private static final long serialVersionUID = 1L;
    public InvalidIdentifierException(){
        super("Syntax Error: Invalid identifier detected.");
    }
    public InvalidIdentifierException(String message){
        super(message);
    }
}
