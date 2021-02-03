package UI;

import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import utils.Utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class MainWin {
    private JButton lexerButton;
    private JPanel panel1;
    private JButton itemSetButton;
    private JButton analysisTableButton;
    private JButton semanticStackButton;
    private JButton interButton;
    private JTabbedPane tabbedPane;
    private JButton grammarButton;
    private final Parser parser;
    public static MySplitPane currentPage;
    public static String cachePath;
    public static Lexer lexer = new Lexer("");

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

        ProgressWin progressWin = new ProgressWin(frame, "初始化中。。。", false);
        parser = new Parser(null, true);
        parser.setProgressWin(progressWin);

        tabbedPane.addChangeListener(e -> {
            currentPage = (MySplitPane) tabbedPane.getSelectedComponent();
            if (currentPage != null) currentPage.highlight();
        });

        ActionListener buttonListener = e -> {
            if (!parser.isInit()) return;

            if (Objects.equals(e.getActionCommand(), "Item Set") ||
                    Objects.equals(e.getActionCommand(), "Analysis Table") ||
                    Objects.equals(e.getActionCommand(), "Grammar")) {
                if (tabbedPane.getTabCount() == 0) {
                    MySplitPane mySplitPane = new MySplitPane();
                    mySplitPane.getSplitPane().setLeftComponent(null);
                    addTab(tabbedPane, "Output:", mySplitPane);
                }
            } else {
                if (currentPage == null || Objects.equals(tabbedPane.getTitleAt(0), "Output:")) {
                    JOptionPane.showMessageDialog(panel1,"当前没有已打开的文件！","提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            if (!currentPage.getBottomPanel().isVisible()) {
                currentPage.getBottomPanel().setVisible(true);
                currentPage.getSplitPane().setDividerLocation(250);
            }

            currentPage.getTextArea().setForeground(Color.BLACK);
            try {
                switch (e.getActionCommand()) {
                    case "Grammar":
                        currentPage.getTextArea().setText(Utils.jsonToGrammar(parser.grammarArray, 9));
                        break;
                    case "Item Set":
                        currentPage.getTextArea().setText(parser.stateSetToString());
                        break;
                    case "Analysis Table":
                        currentPage.getTextArea().setText(parser.tableToString(9));
                        break;
                    case "Lexer":
                        currentPage.getTextArea().setText(lexer.toString(6));
                        break;
                    case "Semantic Stack":
                        parser.setLexer(new Lexer(currentPage.getTextPane().getText()));
                        parser.startAnalyze();
                        currentPage.getTextArea().setText(parser.stackToString());
                        break;
                    case "Inter":
                        parser.setLexer(new Lexer(currentPage.getTextPane().getText()));
                        parser.startAnalyze();
                        currentPage.getTextArea().setText(parser.interToString());
                }
                currentPage.getTextArea().setCaretPosition(0);
            } catch (Error error) {
                JTextArea currentTextArea = currentPage.getTextArea();
                JTextPane currentTextPane = currentPage.getTextPane();
                Token currentToken = parser.getCurrentToken();

                Document doc = currentTextPane.getDocument();
                SimpleAttributeSet set = new SimpleAttributeSet();
                StyleConstants.setBackground(set, Color.red);

                currentTextArea.setForeground(Color.red);
                currentTextArea.setText(error.toString());
                try {
                    doc.remove(currentToken.startIndex, currentToken.length());
                    doc.insertString(currentToken.startIndex, currentToken.toString(), set);
                } catch (BadLocationException badLocationException) {
                    System.err.println("start:" + currentToken.startIndex + " length:" + currentToken.length());
                    badLocationException.printStackTrace();
                }
            }
        };

        grammarButton.addActionListener(buttonListener);
        itemSetButton.addActionListener(buttonListener);
        analysisTableButton.addActionListener(buttonListener);
        lexerButton.addActionListener(buttonListener);
        semanticStackButton.addActionListener(buttonListener);
        interButton.addActionListener(buttonListener);

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
