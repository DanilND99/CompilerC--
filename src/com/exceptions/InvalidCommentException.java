package com.exceptions;

public class InvalidCommentException extends Exception{
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     */
    private static final long serialVersionUID = 1L;
    public InvalidCommentException(){
        super("Invalid use of close comment statement.");
    }
    public InvalidCommentException(String message){
        super(message);
    }
}
