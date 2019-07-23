package lexer;

public class Token {

    public final String tag;

    public Token(String tag) {
        this.tag = tag;
    }

    public void show(String end) {
        if (end == null) end = "";
        System.out.print(tag + end);
    }

    public String toString() {
        return tag;
    }

}
