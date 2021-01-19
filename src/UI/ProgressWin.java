package UI;

import javax.swing.*;

public class ProgressWin extends JDialog {
    private JPanel panel;
    private JProgressBar progressBar;

    ProgressWin(JFrame frame, String title, boolean modal) {
        super(frame, title, modal);

        add(panel);
        setSize(400, 150);
        setLocation(frame.getX() + (frame.getWidth() - this.getWidth()) / 2,
                frame.getY() + (frame.getHeight() - this.getHeight()) / 2);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        progressBar.addChangeListener(e -> {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException interruptedException) {
//                interruptedException.printStackTrace();
//            }
            if (progressBar.getValue() == 100) dispose();
        });

    }

    public void setValue(int val) {
        progressBar.setValue(val);
    }

    public void setString(String s) {
        progressBar.setString(s);
    }
}
