package com.analyzers;
import java.util.ArrayList;
import com.utils.*;
import com.exceptions.*;
public class Syntax {
    int tokenCounter = 0;
    Table tokTable = new Table("Token");
    Table idTable = new Table("Identifier Symbol");
    Table numTable = new Table("Number Symbol");
    ArrayList<Token> stream = new ArrayList<Token>();

    void syntaxMain(Table tokTable, Table idTable, Table numTable, ArrayList<Token> stream) throws Exception{
        stream.add(new Token(tokTable.getAndSetToken("$", 3), 0));
        this.tokTable = tokTable;
        this.idTable = idTable;
        this.numTable = numTable;
        this.stream = stream;
        
        program();
    }

    //Match

    void program() throws Exception{

    }

    void declarationList() throws Exception{
        
    }
    
    void declaration() throws Exception{
        
    }

    void declarationType() throws Exception{

    }

    void varEnding() throws Exception{
        
    }
    
    void params() throws Exception{
        
    }

    void paramsList() throws Exception{

    }

    void param() throws Exception{
        
    }
    
    void paramEnding() throws Exception{
        
    }

    void localDeclarations() throws Exception{

    }

    void statementList() throws Exception{
        
    }
    
    void statement() throws Exception{
        
    }

    void elsePart() throws Exception{

    }

    void returnEnding() throws Exception{
        
    }
    
    void var() throws Exception{
        
    }

    void arrayPart() throws Exception{

    }

    void expression() throws Exception{
        
    }
    
    void expressionEnding() throws Exception{
        
    }

    void relop() throws Exception{

    }

    void arithmeticExpression() throws Exception{
        
    }
    
    void arithmeticLoop() throws Exception{
        
    }

    void addop() throws Exception{

    }

    void term() throws Exception{
        
    }
    
    void termLoop() throws Exception{
        
    }

    void mulop() throws Exception{

    }

    void factor() throws Exception{
        
    }
    
    void call() throws Exception{
        
    }

    void args() throws Exception{

    }

    void argsList() throws Exception{

    }

    void argsLoop() throws Exception{

    }

    /*
    void generic() throws Exception{

    }
    */
}
