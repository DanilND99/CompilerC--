package com.exceptions;

public class InvalidCloseCommentException extends Exception{
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     The special exception when an invalid comment closure is detected.
     */
    private static final long serialVersionUID = 1L;
    public InvalidCloseCommentException(){
        super("Invalid use of close comment statement.");
    }
    public InvalidCloseCommentException(String message){
        super(message);
    }
}
