package UI;

import lexer.Token;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MySplitPane extends JPanel{
    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private JTextPane textPane;
    private JPanel bottomPanel;
    private JLabel closeButton;
    private JTextArea textArea;
    private int caretPosition = 0;
    private int lastCaretPosition = 0;
    private int maxLines = 0;

    MySplitPane() {
        Font pageFont = new Font("Consolas", Font.PLAIN, 16);
        setLayout(new BorderLayout());

        JPopupMenu popupMenu = new JPopupMenu();          // 右键菜单
        JMenuItem copyFile = new JMenuItem("复制");
        JMenuItem pasteFile = new JMenuItem("粘贴");
        JMenuItem closePage = new JMenuItem("关闭页面");
        popupMenu.add(copyFile);
        popupMenu.add(pasteFile);
        popupMenu.add(closePage);

        MouseAdapter mouseAdapter = new MouseAdapter() {   // 右键菜单监听器
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        };

        int preferredSize = 99;
        LineNumber lineNumber = new LineNumber();
        lineNumber.setLine(preferredSize);
        textPane.setFont(pageFont);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        setTabs(textPane, 4);
        scrollPane.setRowHeaderView(lineNumber);

        textPane.addMouseListener(mouseAdapter);
        textPane.addCaretListener(e -> {
            int lines = (int) Math.ceil(scrollPane.getViewport().getViewSize().height / 20.0);

            if (maxLines == lines || lines < preferredSize) return;
            lineNumber.setLine(lines);
            maxLines = lines;
        });
        textPane.addKeyListener(new KeyAdapter() {
            boolean isTextChange = false;
            boolean isFirstTime = true;         // 用于使长按键盘keyPressed只执行一次
            int textLength = textPane.getText().length();

            @Override
            public void keyPressed(KeyEvent e) {
                if (!isFirstTime) return;
                lastCaretPosition = textPane.getCaretPosition();
                isFirstTime = false;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                isFirstTime = true;
                caretPosition = textPane.getCaretPosition();
                int length = textPane.getText().length();
                if (textLength != length) {
                    isTextChange = true;
                    textLength = length;
                }
                if (!isTextChange) return;

                highlight();
                isTextChange = false;
            }
        });

        closeButton.addMouseListener(new MouseAdapter() {   // 输出窗口关闭按钮监听器
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
                bottomPanel.setVisible(false);
            }
        });

        add("Center", splitPane);
    }

    public void highlight() {
        int changePoint = Math.min(lastCaretPosition, caretPosition);
        MainWin.lexer.setReader(textPane.getText(), changePoint);
        Document doc = textPane.getDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();

        Token token = null;
        try {
            while ((token = MainWin.lexer.getToken()) != null) {
                StyleConstants.setForeground(set, token.getColor());
                int temp = caretPosition;

                doc.remove(token.startIndex, token.length());
                doc.insertString(token.startIndex, token.toString(), set);
                textPane.setCaretPosition(temp);
            }
        } catch (BadLocationException badLocationException) {
            System.err.println("Token:" + token.toString() + " start:" + token.startIndex + " length:" + token.length());
            badLocationException.printStackTrace();
        }
    }

    public static void setTabs(final JTextPane textPane, int charactersPerTab) {   // 设置textPane的tab长度
        FontMetrics fontMetrics = textPane.getFontMetrics(textPane.getFont());
        int charWidth = fontMetrics.charWidth(' ');
        int tabWidth = charWidth * charactersPerTab;
        TabStop[] tabs = new TabStop[5];

        for (int j = 0; j < tabs.length; j++) {
            int tab = j + 1;
            tabs[j] = new TabStop(tab * tabWidth);
        }

        TabSet tabSet = new TabSet(tabs);
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setTabSet(attributes, tabSet);
        int length = textPane.getDocument().getLength();
        textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
    }

    public JPanel getBottomPanel() {
        return bottomPanel;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
