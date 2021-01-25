package UI;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class LineNumber extends JPanel {

    private final GridLayout gridLayout = new GridLayout(1, 1, 0, 0);
    private final LinkedList<JLabel> list = new LinkedList<>();
    private int lines = 0;

    LineNumber() {
        setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        setLayout(gridLayout);
    }

    public void setLine(int line) {
        gridLayout.setRows(line);
        if (line > lines) {
            for (int i = lines+1; i <= line; i++) {
                JLabel label = new JLabel(String.valueOf(i), JLabel.RIGHT);
                label.setForeground(Color.gray);
                add(label);
                list.add(label);
            }
        }
        else {
            for (int i = lines; i > line; i--) {
                remove(list.getLast());
                list.removeLast();
            }
        }
        lines = line;
    }
}
