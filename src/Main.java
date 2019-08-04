import lexer.Lexer;
import parser.Parser;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        //System.out.println(args[0]);
        Lexer lexer = new Lexer();
        //lexer.show("  ");
        Parser parser = new Parser(lexer);
        //parser.showStateSet();
        //parser.showTable(9);
        parser.showStack();
        parser.showInterCode();
    }
}
