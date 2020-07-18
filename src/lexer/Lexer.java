package lexer;

import java.io.*;
import java.util.Hashtable;

public class Lexer {

    public int lines = 1;
    private char current_c;
    private char next_c = ' ';
    private Reader reader;
    private final Hashtable<String, Word> words = new Hashtable<>();

    public Lexer(String path) throws IOException {
        init(path);
    }

    private void init(String path) throws IOException{
        File file = new File(path);
        if (!file.exists()) throw new Error("文件不存在！请检查路径是否正确 " + file.getAbsolutePath());
        reader = new InputStreamReader(new FileInputStream(file));

        save(Type.Bool);
        save(Type.Float);
        save(Type.Int);
        save(Type.Char);
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

    private void readChar() throws IOException {
        current_c = next_c;
        next_c = (char) reader.read();
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
            if (current_c == (char) -1) return null;
            if (current_c == '\r' && next_c == '\n') {
                lines += 1;
                next_line = false;
            }
            if (current_c == '/' && next_c == '/') next_line = true;
        } while (next_line || Character.isWhitespace(current_c));

        switch (current_c) {
            case '&':
                if (readChar('&')) return Word.and;
                else return new Token("&");
            case '|':
                if (readChar('|')) return Word.or;
                else return new Token("|");
            case '=':
                if (readChar('=')) return Word.equal;
                else return new Token("=");
            case '!':
                if (readChar('=')) return Word.unequal;
                else return new Token("!");
            case '<':
                if (readChar('=')) return Word.le;
                else return new Token("<");
            case '>':
                if (readChar('=')) return Word.ge;
                else return new Token(">");
        }

        if (Character.isDigit(current_c)) {
            boolean isInteger = true;
            do {
                buffer.append(current_c);
                if (Character.isLetter(next_c)) break;
                if (readChar('.')) {
                    buffer.append('.');
                    isInteger = false;
                }
                else if (!Character.isDigit(next_c)) {
                    if (isInteger) return new Imm<>(Integer.parseInt(buffer.toString()));
                    else return new Imm<>(Float.parseFloat(buffer.toString()));
                }
                readChar();
            } while (Character.isDigit(current_c));
            throw new Error("在第" + this.lines + "行附近可能有错，检测到出错的词法单元：“" + current_c + "” \n");
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
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }

        if (current_c == (char) -1) return null;
        return new Token(current_c + "");     // 其他所有字符
    }

    public void show(String end) throws IOException {
        Token token;
        while ((token = getToken()) != null) {
            token.show(end);
        }
    }

}
