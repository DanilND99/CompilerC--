package com.analyzers;
import java.util.ArrayList;
import com.utils.*;
import com.exceptions.*;
public class Syntax {
    int tokenPointer = 0;
    String tokenValue;
    //Copies of the tables and stream to not pass them in every function.
    Table tokTable = new Table("Token");
    Table idTable = new Table("Identifier Symbol");
    Table numTable = new Table("Number Symbol");
    ArrayList<Token> stream = new ArrayList<Token>();

    public void syntaxMain(Table tokTable, Table idTable, Table numTable, ArrayList<Token> stream) throws Exception{
        stream.add(new Token(tokTable.getAndSetToken("$", 3), 0));
        this.tokTable = tokTable;
        this.idTable = idTable;
        this.numTable = numTable;
        this.stream = stream;
        tokenValue = tokTable.getValue(stream.get(tokenPointer).getTokenEntry());
        program();
        if(tokenValue.equals("$")){
            idTable = this.idTable;
            idTable.printIdentifierTable();
            System.out.println("Syntax Analysis finished, no errors found.");
        }else{
            throw new GenericSyntaxException("Unexpected end of file. Token: " + tokenPointer);
        }
    }

    //Match
    void match(String expected) throws Exception{
        if(expected.equals(tokenValue)){
            tokenPointer++;
            tokenValue = tokTable.getValue(stream.get(tokenPointer).getTokenEntry());
        }else{
            throw new GenericSyntaxException("At line: " + stream.get(tokenPointer).getLine() + " unexpected token: " + tokenValue +", expected: " + expected);
        }
    }

    void program() throws Exception{
        System.out.println("Entro program");
        if(tokenValue.equals("int") || tokenValue.equals("void")){
            declaration();
            declarationList();
        }else{
            throw new GenericSyntaxException("Invalid beginning of file, must start with int or void Token: " + tokenPointer);
        }
    }

    void declarationList() throws Exception{
        System.out.println("Entro declarationList");
        if(tokenValue.equals("int") || tokenValue.equals("void")){
            declaration();
            declarationList();
        }else if (tokenValue.equals("$")){
            return;
        }else{
            throw new GenericSyntaxException("Invalid declaration, must start with int or void Token: " + tokenPointer);
        }
    }
    
    void declaration() throws Exception{
        System.out.println("Entro declaration");
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
            throw new GenericSyntaxException("Invalid declaration, must start with int or void Token: " + tokenPointer);
        }
    }

    void declarationType() throws Exception{
        System.out.println("Entro declarationType");
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
            throw new GenericSyntaxException("Wrong variable or function declaration, expected [ ( or ; Token: " + tokenPointer);
        }
    }

    void varEnding() throws Exception{
        System.out.println("Entro varEnding");
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
            throw new GenericSyntaxException("Wrong variable declaration, expected ; or [ Token: " + tokenPointer);
        }
    }
    
    void params() throws Exception{
        System.out.println("Entro params");
        if(tokenValue.equals("int")){
            param();
            paramsList();
        }else if(tokenValue.equals("void")){
            match("void");
        }else{
            throw new GenericSyntaxException("Wrong function parameters, expected void or a list of parameter. Token: " + tokenPointer);
        }
    }

    void paramsList() throws Exception{
        System.out.println("Entro parmasList");
        if(tokenValue.equals(",")){
            match(",");
            param();
            paramsList();
        }else if(tokenValue.equals(")")){
            return;
        }else{
            throw new GenericSyntaxException("Wrong function parameters, expected , or ) Token: " + tokenPointer);
        }
    }

    void param() throws Exception{
        System.out.println("Entro param");
        if(tokenValue.equals("int")){
            match("int");
            match("identifier");
            paramEnding();
        }else{
            throw new GenericSyntaxException("Wrong function parameters, expected an int declaration Token: " + tokenPointer);
        }
    }
    
    void paramEnding() throws Exception{
        System.out.println("Entro paramEnding");
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
            throw new GenericSyntaxException("Wrong function parameters, invalid variable declaration Token: " + tokenPointer);
        }
    }

    void localDeclarations() throws Exception{
        System.out.println("Entro localDeclarations");
        if(tokenValue.equals("int")){
            match("int");
            match("identifier");
            varEnding();
            localDeclarations();
        }else if(tokenValue.equals("identifier") || tokenValue.equals("{") || tokenValue.equals("if") || tokenValue.equals("while") || tokenValue.equals("return") || tokenValue.equals("input") || tokenValue.equals("output") || tokenValue.equals("}")){
            return;
        }else{
            throw new GenericSyntaxException("Wrong local declaration, expected a variable declaration, a statement or an end bracket. Token: " + tokenPointer);
        }
    }

    void statementList() throws Exception{
        System.out.println("Entro statementList");
        if(tokenValue.equals("identifier") || tokenValue.equals("{") || tokenValue.equals("if") || tokenValue.equals("while") || tokenValue.equals("return") || tokenValue.equals("input") || tokenValue.equals("output")){
            statement();
            statementList();
        }else if(tokenValue.equals("}")){
            return;
        }else{
            System.out.println("Entro statement list");
            throw new GenericSyntaxException("Wrong list of statements, expected a statement or an end bracket. Token: " + tokenPointer + " Value: " + tokenValue);
        }
    }
    
    void statement() throws Exception{
        System.out.println("Entro statement");
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
            throw new GenericSyntaxException("Wrong beginning of statement. Token: " + tokenPointer);
        }
    }

    void varOrCallStatement() throws Exception{
        System.out.println("Entro varOrCallStatement");
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
            throw new GenericSyntaxException("Expected an assignation, an array or a function call Token: " + tokenPointer);
        }
    }

    void elsePart() throws Exception{
        System.out.println("Entro else part");
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
                throw new GenericSyntaxException("Wrong list of statements, expected a statement or an end bracket. Token: " + tokenPointer);
        }
    }

    void returnEnding() throws Exception{
        System.out.println("Entro returnEnding");
        if(tokenValue.equals(";")){
            match(";");
        }else if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            expression();
            match(";");
        }else{
            throw new GenericSyntaxException("Wrong end of return, expected ; or an expression. Token: " + tokenPointer);
        }
    }

    void arrayPart() throws Exception{
        System.out.println("Entro arrayPart");
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
                throw new GenericSyntaxException("Expected an array, an addop, mulop, relop, = , ; , ',', ] or ) Token: " + tokenPointer);
        }
    }

    void expression() throws Exception{
        System.out.println("Entro expression");
        if(tokenValue.equals("identifier") || tokenValue.equals("number") || tokenValue.equals("(")){
            arithmeticExpression();
            expressionEnding();
        }else{
            throw new GenericSyntaxException("Wrong expression, expected an identifier, number or ( Token: " + tokenPointer);
        }
    }
    
    void expressionEnding() throws Exception{
        System.out.println("Entro expressionEnding");
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
                throw new GenericSyntaxException("Wrong end of expression, expected a relop, ; or ). Token: " + tokenPointer);
        }
    }

    void relop() throws Exception{
        System.out.println("Entro relop");
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
                throw new GenericSyntaxException("Wrong relop. Token: " + tokenPointer);
        }
    }

    void arithmeticExpression() throws Exception{
        System.out.println("Entro arithmeticExpression");
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            term();
            arithmeticLoop();
        }else{
            throw new GenericSyntaxException("Wrong arithmetic expression, expected an identifier, number or (. Token: " + tokenPointer);
        }
    }
    
    void arithmeticLoop() throws Exception{
        System.out.println("Entro arithmeticLoop");
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
                throw new GenericSyntaxException("Wrong arithmetic loop, expected an addop, relop, ',', ; , ] or ) Token: " + tokenPointer);
        }
    }

    void addop() throws Exception{
        System.out.println("Entro addop");
        if(tokenValue.equals("+")){
            match("+");
        }else if(tokenValue.equals("-")){
            match("-");
        }else{
            throw new GenericSyntaxException("Wrong addop, expected + or -");
        }
    }

    void term() throws Exception{
        System.out.println("Entro term");
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            factor();
            termLoop();
        }else{
            throw new GenericSyntaxException("Wrong term, expected an identifier, a number or ( Token: " + tokenPointer);
        }
    }
    
    void termLoop() throws Exception{
        System.out.println("Entro termLoop");
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
                throw new GenericSyntaxException("Wrong term loop, expected a mulop, addop, relop, ',', ;, ], ) Token: " + tokenPointer + " Value: " + tokenValue);
        }
    }

    void mulop() throws Exception{
        System.out.println("Entro mulop");
        if(tokenValue.equals("/")){
            match("/");
        }else if(tokenValue.equals("*")){
            match("*");
        }else{
            throw new GenericSyntaxException("Wrong mulop, expected * or / Token: " + tokenPointer);
        }
    }

    void factor() throws Exception{
        System.out.println("Entro factor");
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
            throw new GenericSyntaxException("Wrong factor, expected an identifier, number or ( Token: " + tokenPointer);
        }
    }
    
    void varOrCall() throws Exception{
        System.out.println("Entro varOrCall");
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
                throw new GenericSyntaxException("Wrong variable or call declaration, expected a mulop, addop, relop, array, ',', ;, ], (, ) Token: " + tokenPointer + " Value xzv: " + tokenValue);
        }
    }

    void args() throws Exception{
        System.out.println("Entro args");
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            argsList();
        }else if(tokenValue.equals(")")){
            return;
        }else{
            throw new GenericSyntaxException("Wrong arguments, expected an identifier, a number or ( Token: " + tokenPointer);
        }
    }

    void argsList() throws Exception{
        System.out.println("Entro argsList");
        if(tokenValue.equals("(") || tokenValue.equals("identifier") || tokenValue.equals("number")){
            arithmeticExpression();
            argsLoop();
        }else{
            throw new GenericSyntaxException("Wrong arguments, expected an identifier, a number or ( Token: " + tokenPointer);
        }
    }

    void argsLoop() throws Exception{
        System.out.println("Entro argsLoop");
        if(tokenValue.equals(",")){
            match(",");
            arithmeticExpression();
            argsLoop();
        }else if(tokenValue.equals(")")){
            return;
        }else{
            throw new GenericSyntaxException("Wrong arguments loop, expected , or ) Token: " + tokenPointer);
        }
    }
}
