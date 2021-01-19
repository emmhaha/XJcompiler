package UI;

import lexer.Lexer;
import parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class MainWin {
    private JButton lexerButton;
    private JPanel panel1;
    private JButton stateSetButton;
    private JButton symbolTableButton;
    private JButton semanticStackButton;
    private JButton interButton;
    private JTabbedPane tabbedPane;
    private JButton grammarButton;
    private Lexer lexer;
    private final Parser parser;
    private MySplitPane currentPage;
    public static String cachePath;

    public MainWin(int width, int height, String srcPath, String cachePath) {
        MainWin.cachePath = cachePath;
        if (cachePath == null) MainWin.cachePath = "cache/";
        JFrame frame = new JFrame("XJcomplier");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setJMenuBar(new MyMenuBar(tabbedPane));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);

        frame.pack();
        frame.setVisible(true);

//        try {
//            page = Utils.readText(srcPath);
//            currentSplitPane.getTextPane().setText(page);
//        } catch (IOException e) {
//            System.out.println("源码打开失败！");
//            e.printStackTrace();
//        }

        ProgressWin progressWin = new ProgressWin(frame, "初始化中。。。", false);
        parser = new Parser(null, true);
        parser.setProgressWin(progressWin);

        tabbedPane.addChangeListener(e -> currentPage = (MySplitPane) tabbedPane.getSelectedComponent());

        grammarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        stateSetButton.addActionListener(e -> {
            if (!parser.isInit()) return;
            if (tabbedPane.getTabCount() == 0) {
                MySplitPane mySplitPane = new MySplitPane();
                mySplitPane.getSplitPane().setLeftComponent(null);
                addTab(tabbedPane, "Output:", mySplitPane);
            }
            if (!currentPage.getBottomPanel().isVisible()) {
                currentPage.getBottomPanel().setVisible(true);
                currentPage.getSplitPane().setDividerLocation(250);
            }

            currentPage.getTextArea().setText(parser.stateSetToString());
            System.out.println(parser.stateSetToString());
        });
        symbolTableButton.addActionListener(e -> {
            if (!parser.isInit()) return;
            if (tabbedPane.getTabCount() == 0) {
                MySplitPane mySplitPane = new MySplitPane();
                mySplitPane.getSplitPane().setLeftComponent(null);
                addTab(tabbedPane, "Output:", mySplitPane);
            }
            if (!currentPage.getBottomPanel().isVisible()) {
                currentPage.getBottomPanel().setVisible(true);
                currentPage.getSplitPane().setDividerLocation(250);
            }

            currentPage.getTextArea().setText(parser.tableToString(6));
            System.out.println(parser.tableToString(6));
        });
        lexerButton.addActionListener(e -> {
            if (!parser.isInit()) return;
            if (currentPage == null || Objects.equals(tabbedPane.getTitleAt(0), "Output:")) {
                JOptionPane.showMessageDialog(panel1,"当前没有已打开的文件！","提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!currentPage.getBottomPanel().isVisible()) {
                currentPage.getBottomPanel().setVisible(true);
                currentPage.getSplitPane().setDividerLocation(250);
            }

            lexer = new Lexer(currentPage.getTextPane().getText());
            currentPage.getTextArea().setText(lexer.toString("\n"));
            System.out.println(lexer.toString("\n"));
        });
        semanticStackButton.addActionListener(e -> {
            if (!parser.isInit()) return;
            if (currentPage == null || Objects.equals(tabbedPane.getTitleAt(0), "Output:")) {
                JOptionPane.showMessageDialog(panel1,"当前没有已打开的文件！","提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!currentPage.getBottomPanel().isVisible()) {
                currentPage.getBottomPanel().setVisible(true);
                currentPage.getSplitPane().setDividerLocation(250);
            }

            parser.setLexer(new Lexer(currentPage.getTextPane().getText()));
            parser.startAnalyze();
            currentPage.getTextArea().setText(parser.stackToString());
            System.out.println(parser.stackToString());
        });
        interButton.addActionListener(e -> {
            if (!parser.isInit()) return;
            if (currentPage == null || Objects.equals(tabbedPane.getTitleAt(0), "Output:")) {
                JOptionPane.showMessageDialog(panel1,"当前没有已打开的文件！","提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!currentPage.getBottomPanel().isVisible()) {
                currentPage.getBottomPanel().setVisible(true);
                currentPage.getSplitPane().setDividerLocation(250);
            }

            parser.setLexer(new Lexer(currentPage.getTextPane().getText()));
            parser.startAnalyze();
            currentPage.getTextArea().setText(parser.interToString());
            System.out.println(parser.interToString());
        });

        parser.startInit();
    }

    public static void addTab(JTabbedPane tabbedPane, String title, Component component) {   // 实现选项卡关闭按钮
        JPanel tab = new JPanel();
        tab.setLayout(new BorderLayout(5, 0));   // 设置borderLayout水平间距
        tab.setOpaque(false);

        JLabel label1 = new JLabel(title);
        JLabel label2 = new JLabel();
        label2.setIcon(new ImageIcon("icon/close_dark.png"));
        label2.setName(tabbedPane.getTabCount() + "");
        label2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabbedPane.removeTabAt(Integer.parseInt(((JLabel) e.getSource()).getName()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label2.setIcon(new ImageIcon("icon/close_dark.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label2.setIcon(new ImageIcon("icon/close_white.png"));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                label2.setIcon(new ImageIcon("icon/close_dark.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label2.setIcon(new ImageIcon("icon/close_white.png"));
            }
        });

        tab.add("West", label1);
        tab.add("East", label2);

        tabbedPane.addTab(title, component);
        tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(component));
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(component), tab);
    }

    public void createUIComponents() {

    }
}
