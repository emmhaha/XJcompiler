package UI;

import lexer.Lexer;
import parser.Parser;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MainWin {
    private JButton lexerButton;
    private JPanel panel1;
    private JButton stateSetButton;
    private JButton symbolTableButton;
    private JButton semanticStackButton;
    private JButton interButton;
    private JTabbedPane tabbedPane;
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private JTextArea textArea1;
    private JLabel closeButton;
    private JPanel bottomPanel;
    private JSplitPane splitPane;
    private Lexer lexer;
    private final Parser parser;
    private String page;
    public static String cachePath;

    public MainWin(int width, int height, String srcPath, String cachePath) {
        MainWin.cachePath = cachePath;
        if (cachePath == null) MainWin.cachePath = "cache/";
        JFrame frame = new JFrame("XJcomplier");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setJMenuBar(new MyMenuBar());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);

        frame.pack();
        frame.setVisible(true);

        tabbedPane.addTab("test", splitPane);

        JPopupMenu popupMenu = new JPopupMenu();          // 右键菜单
        JMenuItem copyFile = new JMenuItem("复制");
        JMenuItem pasteFile = new JMenuItem("粘贴");
        JMenuItem closePage = new JMenuItem("关闭页面");
        popupMenu.add(copyFile);
        popupMenu.add(pasteFile);
        popupMenu.add(closePage);

        try {
            page = Utils.readText(srcPath);
            textPane.setText(page);
            //lexer = new Lexer(page);

        } catch (IOException e) {
            System.out.println("源码打开失败！");
            e.printStackTrace();
        }

        parser = new Parser(null, true);
        parser.startInit();
        lexerButton.addActionListener(e -> {
            if (!bottomPanel.isVisible()) {
                bottomPanel.setVisible(true);
                splitPane.setDividerLocation(250);
            }

            lexer = new Lexer(textPane.getText());
            textArea1.setText(lexer.toString("\n"));
            System.out.println(lexer.toString("\n"));
        });
        stateSetButton.addActionListener(e -> {
            if (!bottomPanel.isVisible()) {
                bottomPanel.setVisible(true);
                splitPane.setDividerLocation(250);
            }

            parser.startInit();
            textArea1.setText(parser.stateSetToString());
            System.out.println(parser.stateSetToString());
        });
        symbolTableButton.addActionListener(e -> {
            if (!bottomPanel.isVisible()) {
                bottomPanel.setVisible(true);
                splitPane.setDividerLocation(250);
            }

            parser.startInit();
            textArea1.setText(parser.tableToString(6));
            System.out.println(parser.tableToString(6));
        });
        semanticStackButton.addActionListener(e -> {
            if (!bottomPanel.isVisible()) {
                bottomPanel.setVisible(true);
                splitPane.setDividerLocation(250);
            }

            parser.setLexer(new Lexer(textPane.getText()));
            parser.startInit();
            parser.startAnalyze();
            textArea1.setText(parser.stackToString());
            System.out.println(parser.stackToString());
        });
        interButton.addActionListener(e -> {
            if (!bottomPanel.isVisible()) {
                bottomPanel.setVisible(true);
                splitPane.setDividerLocation(250);
            }

            parser.setLexer(new Lexer(textPane.getText()));
            parser.startInit();
            parser.startAnalyze();
            textArea1.setText(parser.interToString());
            System.out.println(parser.interToString());
        });
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        };
        textPane.addMouseListener(mouseAdapter);

        closeButton.setIcon(new ImageIcon("icon/close_dark.png"));
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setIcon(new ImageIcon("icon/close_dark.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setIcon(new ImageIcon("icon/close_white.png"));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                closeButton.setIcon(new ImageIcon("icon/close_dark.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                closeButton.setIcon(new ImageIcon("icon/close_white.png"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(2);
                bottomPanel.setVisible(false);
            }
        });
    }

    public void createUIComponents() {

    }
}
