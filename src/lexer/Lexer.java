package lexer;

import javax.swing.*;
import java.io.*;
import java.util.Hashtable;

public class Lexer {

    private int lines = 1;
    private char current_c;
    private char next_c = ' ';
    private Reader reader;
    private Hashtable<String, Word> words = new Hashtable<>();

    public Lexer() throws IOException {
        String path = "D:\\MY\\Projects\\IDEA\\XJcompiler\\out\\artifacts\\XJcompiler_jar\\qwe.txt";
        init(path);
    }

    private void init(String path) throws IOException{
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在！请检查路径是否正确");
            return;
        }
        Reader reader = new InputStreamReader(new FileInputStream(file));
        this.reader = reader;

        save(Type.Bool);
        save(Type.Float);
        save(Type.Int);
        save(Word.True);
        save(Word.False);
        save(new Word("if", Tag.IF));
        save(new Word("else", Tag.ELSE));
        save(new Word("while", Tag.WHILE));
        save(new Word("do", Tag.DO));
        save(new Word("break", Tag.BREAK));
    }

    private void save(Word w) {
        words.put(w.value, w);
    }

    private char readChar() throws IOException {
        current_c = next_c;
        next_c = (char) reader.read();
        return current_c;
    }

    private boolean readChar(char c) throws IOException {
        if (next_c != c) return false;
        readChar();
        return true;
    }

    public Token getToken() throws IOException {
        StringBuilder buffer = new StringBuilder();
        boolean next_line = false;
        do {                                           // 去除空白，每次调用这个函数时，current_c指向上一个词法单元的末尾
            readChar();                                // 所以要先读入下一个字符
            if (current_c == '\r' && next_c == '\n') {
                lines += 1;
                next_line = false;
            }
            if (current_c == '/' && next_c == '/') next_line = true;
        } while (next_line || Character.isWhitespace(current_c));

        switch (current_c) {
            case '&':
                if (readChar('&')) return Word.and;
                else return new Token('&');
            case '|':
                if (readChar('|')) return Word.or;
                else return new Token('|');
            case '=':
                if (readChar('=')) return Word.equal;
                else return new Token('=');
            case '!':
                if (readChar('=')) return Word.unequal;
                else return new Token('!');
            case '<':
                if (readChar('=')) return Word.le;
                else return new Token('<');
            case '>':
                if (readChar('=')) return Word.ge;
                else return new Token('>');
        }

        if (Character.isDigit(current_c)) {
            boolean isInt = true;
            do {
                buffer.append(current_c);
                if (readChar('.')) {
                    buffer.append('.');
                    isInt = false;
                }
                else if (!Character.isDigit(next_c)) {
                    if (isInt) return new Imm<>(Integer.parseInt(buffer.toString()));
                    else return new Imm<>(Float.parseFloat(buffer.toString()));
                }
                readChar();
            } while (Character.isDigit(current_c));
            //return null; 运行到这里表示错误的词法单元
        }

        if (Character.isLetter(current_c)) {
            while (true) {
                buffer.append(current_c);
                if (!Character.isLetterOrDigit(next_c)) break;
                readChar();
            }
            String s = buffer.toString();
            Word w = words.get(s);
            if (w != null) return w;
            return new Word(s, Tag.ID);
        }

        if (current_c == (char) -1) return null;
        return new Token(current_c);     // 所有字符
    }

}
