package com.analyzers;
import java.util.ArrayList;
import com.utils.*;
import com.exceptions.GenericSyntaxException;
public class Syntax {
    //Variables to maintain the pointer to the token stream and the current token to match.
    int tokenPointer = 0;
    String tokenValue;
    //Copies of the tables and stream to not pass them in every function.
    Table tokTable = new Table("Token");
    Table idTable = new Table("Identifier Symbol");
    Table numTable = new Table("Number Symbol");
    ArrayList<Token> stream = new ArrayList<Token>();

    //Main function for the Syntax analyzer where calls the starting non-terminal symbol and checks if we arrived at the end of the file.
    public void syntaxMain(Table tokTable, Table idTable, Table numTable, ArrayList<Token> stream) throws Exception{
        //Add the end of file token
        stream.add(new Token(tokTable.getAndSetToken("$", 3), 0));
        //Copy all the parameters
        this.tokTable = tokTable;
        this.idTable = idTable;
        this.numTable = numTable;
        this.stream = stream;
        //Get the first token
        tokenValue = tokTable.getValue(stream.get(tokenPointer).getTokenEntry());
        //Start the recursive descent algorithm
        program();
        //Check for the end of file token.
        if(tokenValue.equals("$")){
            //Update the Identifier Symbol Table
            idTable = this.idTable;
            idTable.printIdentifierTable();
            System.out.println("Syntax Analysis finished, no errors found.");
        }else{
            throw new GenericSyntaxException("Unexpected end of file. Token: " + tokenValue);
        }
    }

    //Checks if the passed token is the same as the current token, if not throws an exception.
    void match(String expected) throws Exception{
        if(expected.equals(tokenValue)){
            //Get the next token.
            tokenPointer++;
            tokenValue = tokTable.getValue(stream.get(tokenPointer).getTokenEntry());
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: " + expected);
        }
    }

    /*
    The CFG programmed in Recursive Descent Approach all the methods below share the same structure.
    An if or switch statement with the first+ values that the non-terminal symbol can have, if there is not a first+ then, throw and exception.
    */
    void program() throws Exception{
        if(tokenValue.equals("int") || tokenValue.equals("void")){
            declaration();
            declarationList();
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: int or void");
        }
    }

    void declarationList() throws Exception{
        if(tokenValue.equals("int") || tokenValue.equals("void")){
            declaration();
            declarationList();
        }else if (tokenValue.equals("$")){
            return;
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: int, void or end of the file");
        }
    }
    
    void declaration() throws Exception{
        if(tokenValue.equals("int")){
            match("int");
            match("identifier");
            declarationType();
        }else if(tokenValue.equals("void")){
            match("void");
            match("identifier");
            //Sets the type of identifier
            idTable.setIdentifierType(stream.get(tokenPointer-1).getSymbolEntry(), "void function");
            match("(");
            params();
            match(")");
            match("{");
            localDeclarations();
            statementList();
            match("}");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: int or void");
        }
    }

    void declarationType() throws Exception{
        if(tokenValue.equals(";") || tokenValue.equals("[")){
            varEnding();
        }else if(tokenValue.equals("(")){
            //Sets the type of identifier
            idTable.setIdentifierType(stream.get(tokenPointer-1).getSymbolEntry(), "int function");
            match("(");
            params();
            match(")");
            match("{");
            localDeclarations();
            statementList();
            match("}");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: [, ( or ;");
        }
    }

    void varEnding() throws Exception{
        if(tokenValue.equals(";")){
            //Sets the type of identifier
            idTable.setIdentifierType(stream.get(tokenPointer-1).getSymbolEntry(), "int variable");
            match(";");
        }else if(tokenValue.equals("[")){
            //Sets the type of identifier
            idTable.setIdentifierType(stream.get(tokenPointer-1).getSymbolEntry(), "int array");
            match("[");
            match("number");
            match("]");
            match(";");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: [ or ;");
        }
    }
    
    void params() throws Exception{
        if(tokenValue.equals("int")){
            param();
            paramsList();
        }else if(tokenValue.equals("void")){
            match("void");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: int or void");
        }
    }

    void paramsList() throws Exception{
        if(tokenValue.equals(",")){
            match(",");
            param();
            paramsList();
        }else if(tokenValue.equals(")")){
            return;
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: , or )");
        }
    }

    void param() throws Exception{
        if(tokenValue.equals("int")){
            match("int");
            match("identifier");
            paramEnding();
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: int");
        }
    }
    
    void paramEnding() throws Exception{
        if(tokenValue.equals("[")){
            //Sets the type of identifier
            idTable.setIdentifierType(stream.get(tokenPointer-1).getSymbolEntry(), "int array");
            match("[");
            match("]");
        }else if(tokenValue.equals(",") || tokenValue.equals(")")){
            //Sets the type of identifier
            idTable.setIdentifierType(stream.get(tokenPointer-1).getSymbolEntry(), "int variable");
            return;
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: [, ) or ,");
        }
    }

    void localDeclarations() throws Exception{
        if(tokenValue.equals("int")){
            match("int");
            match("identifier");
            varEnding();
            localDeclarations();
        }else if(tokenValue.equals("identifier") || tokenValue.equals("{") || tokenValue.equals("if") || tokenValue.equals("while") || tokenValue.equals("return") || tokenValue.equals("input") || tokenValue.equals("output") || tokenValue.equals("}")){
            return;
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: int, identifier, if, while, return, input, output, { or }");
        }
    }

    void statementList() throws Exception{
        if(tokenValue.equals("identifier") || tokenValue.equals("{") || tokenValue.equals("if") || tokenValue.equals("while") || tokenValue.equals("return") || tokenValue.equals("input") || tokenValue.equals("output")){
            statement();
            statementList();
        }else if(tokenValue.equals("}")){
            return;
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, if, while, return, input, output, { or }");
        }
    }
    
    void statement() throws Exception{
        if(tokenValue.equals("identifier")){
            match("identifier");
            varOrCallStatement();
        }else if(tokenValue.equals("{")){
            match("{");
            localDeclarations();
            statementList();
            match("}");
        }else if(tokenValue.equals("if")){
            match("if");
            match("(");
            expression();
            match(")");
            statement();
            elsePart();
        }else if(tokenValue.equals("while")){
            match("while");
            match("(");
            expression();
            match(")");
            statement();
        }else if(tokenValue.equals("return")){
            match("return");
            returnEnding();
        }else if(tokenValue.equals("input")){
            match("input");
            match("identifier");
            arrayPart();
            match(";");
        }else if(tokenValue.equals("output")){
            match("output");
            expression();
            match(";");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, if, while, return, input, output or {");
        }
    }

    void varOrCallStatement() throws Exception{
        if(tokenValue.equals("=") || tokenValue.equals("[")){
            arrayPart();
            match("=");
            expression();
            match(";");
        }else if(tokenValue.equals("(")){
            match("(");
            args();
            match(")");
            match(";");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: (, [ or =");
        }
    }

    void elsePart() throws Exception{
        switch(tokenValue){
            case "else":
                match("else");
                statement();
                break;
            case "identifier":
            case "{":
            case "}":
            case "if":
            case "while":
            case "return":
            case "input":
            case "output":
                break;
            default:
                System.out.println("Entro else part");
                throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, if, else, while, return, input, output, { or }");
        }
    }

    void returnEnding() throws Exception{
        if(tokenValue.equals(";")){
            match(";");
        }else if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            expression();
            match(";");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, number, ( or ;");
        }
    }

    void arrayPart() throws Exception{
        switch(tokenValue){
            case "[":
                match("[");
                arithmeticExpression();
                match("]");
                break;
            case ";":
            case "=":
            case "*":
            case "/":
            case "+":
            case "-":
            case "<=":
            case "<":
            case ">":
            case ">=":
            case "==":
            case "!=":
            case ",":
            case "]":
            case ")":
                break;
            default:
                throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: <=, <, >, >=, ==, !=, =, *, /, +, -, [, ], ), ; or ,");
        }
    }

    void expression() throws Exception{
        if(tokenValue.equals("identifier") || tokenValue.equals("number") || tokenValue.equals("(")){
            arithmeticExpression();
            expressionEnding();
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, number or (");
        }
    }
    
    void expressionEnding() throws Exception{
        switch(tokenValue){
            case "<=":
            case "<":
            case ">":
            case ">=":
            case "==":
            case "!=":
                relop();
                arithmeticExpression();
                break;
            case ")":
            case ";":
                break;
            default:
                throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: <=, <, >, >=, ==, !=, ) or ;");
        }
    }

    void relop() throws Exception{
        switch(tokenValue){
            case "<=":
                match("<=");
                break;
            case "<":
                match("<");
                break;
            case ">":
                match(">");
                break;
            case ">=":
                match(">=");
                break;
            case "==":
                match("==");
                break;
            case "!=":
                match("!=");
                break;
            default:
                throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: <=, <, >, >=, == or !=");
        }
    }

    void arithmeticExpression() throws Exception{
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            term();
            arithmeticLoop();
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, number or (");
        }
    }
    
    void arithmeticLoop() throws Exception{
        switch(tokenValue){
            case "+":
            case "-":
                addop();
                term();
                arithmeticLoop();
                break;
            case "<=":
            case "<":
            case ">=":
            case ">":
            case "==":
            case "!=":
            case ";":
            case ",":
            case "]":
            case ")":
                break;
            default:
                throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: <=, <, >, >=, ==, !=, +, -, ], ), ; or ,");
        }
    }

    void addop() throws Exception{
        if(tokenValue.equals("+")){
            match("+");
        }else if(tokenValue.equals("-")){
            match("-");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: + or -");
        }
    }

    void term() throws Exception{
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            factor();
            termLoop();
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, number or (");
        }
    }
    
    void termLoop() throws Exception{
        switch(tokenValue){
            case "/":
            case "*":
                mulop();
                factor();
                termLoop();
                break;
            case "+":
            case "-":
            case "<=":
            case "<":
            case ">=":
            case ">":
            case "==":
            case "!=":
            case ";":
            case ",":
            case "]":
            case ")":
                break;
            default:
                throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: <=, <, >, >=, ==, !=, *, /, +, -, ], ), ; or ,");
        }
    }

    void mulop() throws Exception{
        if(tokenValue.equals("/")){
            match("/");
        }else if(tokenValue.equals("*")){
            match("*");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: * or /");
        }
    }

    void factor() throws Exception{
        if(tokenValue.equals("number")){
            match("number");
        }else if(tokenValue.equals("identifier")){
            match("identifier");
            varOrCall();
        }else if(tokenValue.equals("(")){
            match("(");
            arithmeticExpression();
            match(")");
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, number or (");
        }
    }
    
    void varOrCall() throws Exception{
        switch(tokenValue){
            case "(":
                match("(");
                args();
                match(")");
                break;
            case "[":
            case "/":
            case "*":
            case "+":
            case "-":
            case "<=":
            case "<":
            case ">=":
            case ">":
            case "==":
            case "!=":
            case ";":
            case ",":
            case "]":
            case ")":
                arrayPart();
                break;
            default:
                throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: <=, <, >, >=, ==, !=, *, /, +, -, [, ], (, ), ; or ,");
        }
    }

    void args() throws Exception{
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            argsList();
        }else if(tokenValue.equals(")")){
            return;
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, number, ( or )");
        }
    }

    void argsList() throws Exception{
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            arithmeticExpression();
            argsLoop();
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: identifier, number or (");
        }
    }

    void argsLoop() throws Exception{
        if(tokenValue.equals(",")){
            match(",");
            arithmeticExpression();
            argsLoop();
        }else if(tokenValue.equals(")")){
            return;
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: ) or ,");
        }
    }
}
