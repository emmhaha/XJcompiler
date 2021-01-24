package lexer;

import java.awt.*;

public class Token {

    public final String tag;
    public int startIndex = 0;         // 词法单元在输入代码中的起始位置
    public int lines;                  // token所在行数

    public Token(String tag) {
        this.tag = tag;
    }

    public Color getColor() {
        switch (tag) {
            case Tag.ID: return Color.MAGENTA;
            case Tag.IMM: return new Color(139,69,19);
            case Tag.DO:
            case Tag.BREAK:
            case Tag.ELSE:
            case Tag.IF:
            case Tag.WHILE:
            case Tag.BASIC_TYPE: return Color.BLUE;
        }
        return Color.black;
    }

    public void show(String end) {
        if (end == null) end = "";
        System.out.print(tag + end);
    }

    public String toString() {
        return tag;
    }

    public int length() {
        return this.toString().length();
    }

}
