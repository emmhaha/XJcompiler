import UI.TestUI;
import inter.Unit;
import lexer.Lexer;
import parser.Parser;
import UI.MainWin;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String srcPath = "src\\test.txt";
        String cachePath = "cache/";

        Utils.initGlobalFont(new Font("微软雅黑", Font.PLAIN, 15));

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }

        //new TestUI();
        new MainWin(1100, 800, srcPath, cachePath);

//        Lexer lexer = new Lexer("\r\n\r\nint");
//        lexer.setReader("\r\n\r\nin", null);
//        lexer.show(6);
//        Parser parser = new Parser(lexer, false);
//        //parser.setLexer(lexer);
//        parser.startInit();
//        parser.startAnalyze();
//        parser.showInterCode();

//        parser.showTable(9);
//        parser.showStack();
//        parser.showInterCode();
    }
}
