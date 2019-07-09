package lexer;

public class Token {

    public final String tag;
    public Integer value;

    Token(String tag) {
        this.tag = tag;
        show();
    }

    Token(String tag, int value) {
        this.tag = tag;
        this.value = value;
        show();
    }

    private void show() {
        System.out.print(toString() + " ");
    }

    public String toString() {
        if (value != null) return Integer.toString(value);
        else return "" + tag;
    }
}
