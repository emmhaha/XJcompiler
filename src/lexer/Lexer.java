package lexer;

import utils.Utils;

import java.util.Hashtable;
import java.util.LinkedList;

public class Lexer {

    public int lines = 1;
    private char current_c;
    private char next_c = ' ';
    private StringBuilder reader;
    private final Hashtable<String, Word> words = new Hashtable<>();
    private final LinkedList<Token> tokens = new LinkedList<>();
    private int readIndex = 0;     // 指向next_c的下一个位置
    private int startIndex = 0;    // 工作区起始下标
    private int currentToken = 0;

    public Lexer(String srcCode) {
        init(srcCode);
    }

    private void init(String srcCode) {
        reader = new StringBuilder(srcCode);

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

        addTokenToList(0);
    }

    public void setReader(String srcCode, Integer changePoint) {    // 重设输入数据
        if (changePoint == null || changePoint < 0) {
            changePoint = findChangePoint(reader.toString(), srcCode);
        }
        reader.delete(0, reader.length());
        reader.append(srcCode);
        addTokenToList(changePoint);
    }

    private void save(Word w) {
        words.put(w.value, w);
    }

    private void readChar() {
        try {
            current_c = next_c;
            next_c = reader.charAt(readIndex++);
        } catch (IndexOutOfBoundsException ignore) {
            next_c = (char) -1;
        }
    }

    private boolean readChar(char c) {
        if (next_c != c) return false;
        readChar();
        return true;
    }

    public void addTokenToList(int changePoint) {      // 变动点是输入代码有改变的第一个位置，输入代码有改动时要重新执行此函数
        if (changePoint != 0) changePoint--;
        readIndex = findStartIndex(changePoint);
        next_c = ' ';
        Token token;
        while ((token = generateToken()) != null) {
            token.lines = lines;
            token.startIndex = startIndex - lines + 1;  // 将\r\n当作一个符号，每个token的开始位置要减去当前行数
            tokens.add(token);
        }
    }

    private int findStartIndex(int changePoint) {
        for (int i = tokens.size()-1; i >= 0; i--) {
            if (changePoint >= tokens.get(i).startIndex) {
                lines = tokens.getLast().lines;
                int index = tokens.get(i).startIndex + lines - 1;   // 之前减去的行数要加回来
                tokens.removeLast();
                return index;
            } else tokens.removeLast();
        }
        lines = 1;
        return 0;
    }

    private int findChangePoint(String oldString, String newString) {
        int i = 0;
        try {
            while (true) {
                if (oldString.charAt(i) == newString.charAt(i)) i++;
                else return i;
            }
        } catch (IndexOutOfBoundsException ignore) {
            return i;
        }
    }

    public Token getToken() {
        if (currentToken > tokens.size()-1) {
            currentToken = 0;
            return null;
        }
        return tokens.get(currentToken++);
    }

    private Token generateToken() {        // 读取输入数据，生成token
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
        startIndex = readIndex - 2;

        switch (current_c) {
            case '&':
                if (readChar('&')) return new Word(Word.and);
                else return new Token("&");
            case '|':
                if (readChar('|')) return new Word(Word.or);
                else return new Token("|");
            case '=':
                if (readChar('=')) return new Word(Word.equal);
                else return new Token("=");
            case '!':
                if (readChar('=')) return new Word(Word.unequal);
                else return new Token("!");
            case '<':
                if (readChar('=')) return new Word(Word.le);
                else return new Token("<");
            case '>':
                if (readChar('=')) return new Word(Word.ge);
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
            throw new Error("\n在第" + this.lines + "行附近可能有错，检测到出错的词素：“" + current_c + "” \n");
        }

        if (Character.isLetter(current_c)) {
            while (true) {
                buffer.append(current_c);
                if (!Character.isLetterOrDigit(next_c)) break;
                readChar();
            }
            String s = buffer.toString();
            Word w = words.get(s);
            if (w != null) {
                if (w.getClass().equals(Word.class)) return new Word(w);
                else return new Type((Type) w);
            }
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }

        if (current_c == (char) -1) return null;
        return new Token(current_c + "");     // 其他所有字符
    }

    public void show(int width) {
        System.out.println(this.toString(width));
    }

    public String toString(int width) {
        Token token;
        StringBuilder s = new StringBuilder();
        while ((token = getToken()) != null) {
            s.append("lines:").append(token.lines).append(Utils.getSpace(width - String.valueOf(token.lines).length()));
            s.append("tag:").append(token.tag).append(Utils.getSpace(width - token.tag.length()));
            s.append("index:").append(token.startIndex).append(Utils.getSpace(width - String.valueOf(token.startIndex).length()));
            s.append("value:").append(token.toString()).append("\n");
        }
        return s.toString();
    }
}
