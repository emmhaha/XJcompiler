package UI;

import utils.Utils;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;

public class MyMenuBar extends JMenuBar {

    private final JTabbedPane tabbedPane;

    MyMenuBar(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;

        add(createFileMenu());    //添加“文件”菜单
        add(createEditMenu());    //添加“编辑”菜单
        setVisible(true);
    }

    //定义“文件”菜单
    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu("文件(F)");
        menu.setMnemonic(KeyEvent.VK_F);    //设置快速访问符

        JMenuItem item = new JMenuItem("新建(N)",KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        item.addActionListener(e -> {
            if (tabbedPane.getTabCount() == 1 && Objects.equals(tabbedPane.getTitleAt(0), "Output:")) tabbedPane.removeAll();
            MainWin.addTab(tabbedPane, "untitled", new MySplitPane());
        });
        menu.add(item);

        item = new JMenuItem("打开(O)",KeyEvent.VK_O);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        item.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int val = fc.showOpenDialog(null);    //文件打开对话框
            if(val == JFileChooser.APPROVE_OPTION) {
                try {
                    if (tabbedPane.getTabCount() == 1 && Objects.equals(tabbedPane.getTitleAt(0), "Output:")) tabbedPane.removeAll();
                    String path = fc.getSelectedFile().toString();
                    MySplitPane newPage = new MySplitPane();

                    MainWin.addTab(tabbedPane, Utils.getFileName(path), newPage);
                    newPage.getTextPane().setText(Utils.readText(fc.getSelectedFile().toString()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(this,"文件打开失败！","错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        menu.add(item);

        item = new JMenuItem("保存(S)",KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        item.addActionListener(e -> {

        });
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("退出(E)",KeyEvent.VK_E);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        item.addActionListener(e -> {

        });
        menu.add(item);
        return menu;
    }

    //定义“编辑”菜单
    private JMenu createEditMenu()
    {
        JMenu menu = new JMenu("编辑(E)");
        menu.setMnemonic(KeyEvent.VK_E);

        JMenuItem item = new JMenuItem("撤销(U)",KeyEvent.VK_U);
        item.setEnabled(false);
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("剪贴(T)",KeyEvent.VK_T);
        menu.add(item);

        item = new JMenuItem("复制(C)",KeyEvent.VK_C);
        menu.add(item);
        menu.addSeparator();

        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("自动换行");
        menu.add(cbMenuItem);
        return menu;
    }
}
