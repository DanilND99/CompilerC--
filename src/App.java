import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import com.utils.*;
import com.analyzers.*;
import com.exceptions.InvalidOpenCommentException;
public class App {
    //Declaration of the Token Table, Token Stream and Symbol tables.
    public static ArrayList<Token> tokenStream = new ArrayList<Token>();
    public static Table tokenTable = new Table("Token");
    public static Table idSymbolTable = new Table("Identifier Symbol");
    public static Table numSymbolTable = new Table("Number Symbol");
    public static void main(String[] args) throws FileNotFoundException, Exception {
        //Declaration of the lexical analyzer.
        Lexical lexAnalyzer = new Lexical();
        Syntax syntaxAnalyzer = new Syntax();
        Scanner reader = new Scanner(System.in);
        System.out.println("Please insert your file path");
        try{
            //Recieves the file path and instanciates the file.
            File file = new File(reader.nextLine());
            Scanner fileReader = new Scanner(file);
            int line = 1;
            while(fileReader.hasNextLine()){
                //Calls the analyze method of the lexical analyzer with every line of the file to fill the tables.
                lexAnalyzer.analyze(fileReader.nextLine(),tokenTable, idSymbolTable, numSymbolTable, tokenStream, line);
                line++;
            }
            fileReader.close();
            //If the file ends in an open comment, throw open comment exception.
            if(lexAnalyzer.getLexType() == 4){
                throw new InvalidOpenCommentException();
            }
            //Prints the tables.
            tokenTable.printTable();
            idSymbolTable.printIdentifierTable();
            numSymbolTable.printTable();
            printTokenStream();
            syntaxAnalyzer.syntaxMain(tokenTable, idSymbolTable, numSymbolTable, tokenStream);
        }catch(FileNotFoundException e){
            //If the file doesn't exist. Returns this exception.
            System.out.println("Exception: File not found.");
        }catch(Exception e){
            //Prints the message of any exception that happens in the analyze method.
            System.out.println(e.getMessage());
        }finally{
            reader.close();
        }
    }

    //Print the token stream.
    public static void printTokenStream(){
        System.out.println("------Token Stream------");
        for(int i = 0; i < tokenStream.size();i++){
            if(tokenStream.get(i).getSymbolEntry() == 0){
                System.out.println("<"+ tokenStream.get(i).getTokenEntry() + ">");
            }else{
                System.out.println("<"+ tokenStream.get(i).getTokenEntry() + "," + tokenStream.get(i).getSymbolEntry() + ">");
            }
        }
        System.out.println("");
        return;
    }
}
