package lexer;

import javax.swing.*;
import java.io.*;

public class Lexer {

    private int lines;
    private char current_c;
    private char next_c = ' ';
    private Reader reader;

    public Lexer() throws IOException {
        String path = "D:\\MY\\Projects\\IDEA\\XJcompiler\\out\\artifacts\\XJcompiler_jar\\qwe.txt";
        init(path);
    }

    void init(String path) throws IOException{
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return;
        }
        Reader reader = new InputStreamReader(new FileInputStream(file));
        this.reader = reader;
    }

    char readChar() throws IOException {
        current_c = next_c;
        return next_c = (char) reader.read();
    }

    boolean readChar(char c) throws IOException {
        readChar();
        if (c == current_c) return true;
        return false;
    }

    public Token getToken() throws IOException {
        StringBuffer buffer = new StringBuffer();
        do {
            readChar();
        } while (Character.isWhitespace(current_c));

            while (current_c != (char)-1) {
            if (current_c == '+') return new Token(Tag.ADD);
            if (current_c == '-') return new Token(Tag.SUB);
            if (current_c == '*') return new Token(Tag.MUL);
            if (current_c == '/') return new Token(Tag.DIV);

            if (Character.isDigit(current_c)) {
                buffer.append(current_c);
                if (!Character.isDigit(next_c)) {
                    return new Token(Tag.NUM, Integer.parseInt(buffer.toString()));
                }
                readChar();
            }
        }
        return null;
    }

}
