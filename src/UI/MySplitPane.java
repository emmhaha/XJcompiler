package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MySplitPane extends JPanel{
    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private JTextPane textPane;
    private JPanel bottomPanel;
    private JLabel closeButton;
    private JTextArea textArea;

    MySplitPane() {
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
        textPane.addMouseListener(mouseAdapter);

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
