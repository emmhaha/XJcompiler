import lexer.Lexer;
import parser.Parser;
import UI.MainWin;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String srcPath = "src\\test.txt";
        String cachePath = "cache/";
        new MainWin(800, 600, srcPath, cachePath);
//        parser.showTable(9);
//        parser.showStack();
//        parser.showInterCode();
    }
}
