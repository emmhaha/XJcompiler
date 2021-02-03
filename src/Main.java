import UI.MainWin;
import lexer.Lexer;
import utils.Utils;

import java.awt.*;
import java.io.IOException;

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

//        Lexer lexer = new Lexer("\r\ni");
//        lexer.setReader("aaaaa\r\ni", 4);
//        lexer.show(9);
//        Parser parser = new Parser(lexer, false);
//        parser.startInit();
//        parser.startAnalyze();
//        parser.showInterCode();

//        parser.showTable(9);
//        parser.showStack();
//        parser.showInterCode();
    }
}
