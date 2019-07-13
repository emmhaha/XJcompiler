package lexer;

public class Token {

    public final int tag;

    Token(int tag) {
        this.tag = tag;
    }

    public void show(String end) {
        if (end == null) end = "";
        System.out.print(toString() + end);
    }

    public String toString() {
        return "" + (char) tag;
    }
}
