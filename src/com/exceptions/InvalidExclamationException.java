package com.exceptions;

public class InvalidExclamationException extends Exception {
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     The special exception when an invalid use of "!" is detected.
     */
    private static final long serialVersionUID = 1L;
    public InvalidExclamationException(){
        super("Invalid use of \"!\".");
    }
    public InvalidExclamationException(String message){
        super(message);
    }
    
}
