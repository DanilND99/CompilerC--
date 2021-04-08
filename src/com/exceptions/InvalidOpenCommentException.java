package com.exceptions;

public class InvalidOpenCommentException extends Exception{
    /**
     serialVersionUID is not needed but the IDE keep asking for it.
     The special exception when a comment is never closed.
     */
    private static final long serialVersionUID = 1L;
    public InvalidOpenCommentException(){
        super("Lexical Error: Comment never closed.");
    }
    public InvalidOpenCommentException(String message){
        super(message);
    }
}
