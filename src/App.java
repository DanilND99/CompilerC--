import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import com.utils.*;
import com.analyzers.*;
public class App {
    //Declaration of the Token and Symbol tables
    public static ArrayList<Symbol> symbolTable = new ArrayList<Symbol>();
    public static TokenTable tokenTable = new TokenTable();
    public static void main(String[] args) throws FileNotFoundException, Exception {
        //Declaration of the lexical analyzer.
        Lexical lexAnalyzer = new Lexical();    
        Scanner reader = new Scanner(System.in);
        System.out.println("Please insert your file path");
        try{
            //Recieves the file path and instanciates the file.
            File file = new File(reader.nextLine());
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNextLine()){
                //Calls the analyze method of the lexical analyzer with every line of the file to fill the tables.
                lexAnalyzer.analyze(fileReader.nextLine(),tokenTable, symbolTable);
            }
            fileReader.close();
            //Prints the tables.
            tokenTable.printTable();
            printSymbolTable();
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

    //Print the symbol table.
    public static void printSymbolTable(){
        System.out.println("------Symbol table------");
        for(int i = 0; i < symbolTable.size();i++){
            System.out.println("Position: "+ symbolTable.get(i).getPosition() + "   Type: " + symbolTable.get(i).getType());
        }
        System.out.println("");
        return;
    }
}
