package com.exceptions;

public class GenericSyntaxException extends Exception{
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     The special exception when an invalid character is detected.
     */
    private static final long serialVersionUID = 1L;

    public GenericSyntaxException(){
        super("Syntax Exception: Generic Error");
    }
    //A constructor to set an specific error message.
    public GenericSyntaxException(String message){
        super("Syntax Exception: " + message);
    }
}
