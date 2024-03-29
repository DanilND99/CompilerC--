package com.exceptions;

public class InvalidNumberException extends Exception {
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     The special exception when an invalid number "1a" is detected.
     */
    private static final long serialVersionUID = 1L;
    public InvalidNumberException(){
        super("Syntax Error: Invalid number detected.");
    }
    public InvalidNumberException(String message){
        super(message);
    }
}
