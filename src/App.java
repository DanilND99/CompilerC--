import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import com.utils.*;
import com.analyzers.*;
public class App {
    public static ArrayList<Symbol> symbolTable = new ArrayList<Symbol>();
    public static TokenTable tokenTable = new TokenTable();
    public static void main(String[] args) throws FileNotFoundException, Exception {
        Lexical lexAnalyzer = new Lexical();
        /*Actual implementation*/
        
        Scanner reader = new Scanner(System.in);
        System.out.println("Please insert your file path");
        try{
            File file = new File(reader.nextLine());
            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNextLine()){
                lexAnalyzer.analyze(fileReader.nextLine(),tokenTable, symbolTable);
            }
            tokenTable.printTable();
            printSymbolTable();
        }catch(FileNotFoundException e){
            System.out.println("Exception: File not found");
        }
        
        /*
        int test;
        lexAnalyzer.setPrevious('!');
        test = lexAnalyzer.charChecker('1');
        System.out.println(test);
        test = lexAnalyzer.charChecker('-');
        System.out.println(test);
        lexAnalyzer.setPrevious('2');
        test = lexAnalyzer.charChecker('a');
        System.out.println(test);
        lexAnalyzer.setPrevious('d');
        test = lexAnalyzer.charChecker('1');
        System.out.println(test);
        lexAnalyzer.setPrevious('*');
        test = lexAnalyzer.charChecker('/');
        System.out.println(test);
        test = lexAnalyzer.charChecker('$');
        System.out.println(test);
        test = lexAnalyzer.charChecker('=');
        System.out.println(test);
        lexAnalyzer.setPrevious('/');
        test = lexAnalyzer.charChecker('*');
        System.out.println(test);
        lexAnalyzer.setPrevious('*');
        test = lexAnalyzer.charChecker('/');
        System.out.println(test);
        lexAnalyzer.setPrevious('\t');
        test = lexAnalyzer.charChecker('a');
        System.out.println(test);
        test = lexAnalyzer.charChecker('1');
        System.out.println(test);
        test = lexAnalyzer.charChecker('+');
        System.out.println(test);
        test = lexAnalyzer.charChecker('\t');
        System.out.println(test);
        test = lexAnalyzer.charChecker('\n');
        System.out.println(test);
        test = lexAnalyzer.charChecker(' ');
        System.out.println(test);
        test = lexAnalyzer.charChecker('A');
        System.out.println(test);
        */

        /*
        lexAnalyzer.iterate("if(x <= 4){\nx++;\n}else{\nx--;\n} ABC@");
        lexAnalyzer.test(tokenTable, symbolTable);
        symbolTable.add(new Symbol(1,"keyword"));
        symbolTable.add(new Symbol(2,"keyword"));
        symbolTable.add(new Symbol(15,"special"));
        symbolTable.add(new Symbol(13,"special"));
        symbolTable.add(new Symbol(6,"keyword"));
        tokenTable.set("variable");
        System.out.println(tokenTable.find("variable")+ "\n");
        System.out.println(tokenTable.getValue(4)+ "\n");
        System.out.println(tokenTable.find("variabe")+ "\n");
        System.out.println(tokenTable.getKey("variable")+ "\n");
        System.out.println(tokenTable.getKey("variabe")+ "\n");
        lexAnalyzer.test(tokenTable, symbolTable);
        tokenTable.printTable();
        printSymbolTable();
        */
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
