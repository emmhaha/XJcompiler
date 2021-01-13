package UI;

import lexer.Lexer;
import parser.Parser;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWin {
    private JButton lexerButton;
    private JPanel panel1;
    private JButton stateSetButton;
    private JButton symbolTableButton;
    private JButton semanticStackButton;
    private JButton interButton;
    private JTextArea textArea;
    private Lexer lexer;
    private Parser parser;
    private String page;
    public static String cachePath;

    public MainWin(int width, int height, String srcPath, String cachePath) {
        MainWin.cachePath = cachePath;
        JFrame frame = new JFrame("XJcomplier");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);

        frame.pack();
        frame.setVisible(true);


        try {
            page = Utils.readText(srcPath);
            textArea.setText(page);
            lexer = new Lexer(page);
            parser = new Parser(lexer, true);
        } catch (IOException e) {
            System.out.println("源码打开失败！");
            e.printStackTrace();
        }

        lexerButton.addActionListener(e -> {
            lexer = new Lexer(page);
            textArea.setText(lexer.toString("   "));
        });
        stateSetButton.addActionListener(e -> {
            parser.startInit();
            textArea.setText(parser.stateSetToString());
        });
        symbolTableButton.addActionListener(e -> {
            parser.startInit();
            textArea.setText(parser.tableToString(6));
        });
        semanticStackButton.addActionListener(e -> {
            parser.startInit();
            parser.startAnalyze();
            textArea.setText(parser.stackToString());
        });
        interButton.addActionListener(e -> {
            parser.startInit();
            parser.startAnalyze();
            textArea.setText(parser.interToString());
        });
    }
}
