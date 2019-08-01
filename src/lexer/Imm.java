package lexer;

public class Imm<T> extends Token {

    private T num;
    public Type type;

    public Imm(T num) {
        super(Tag.IMM);
        this.num = num;
        if (isInt()) type = Type.Int;
        else type = Type.Float;
    }

    private boolean isInt() {
        return num instanceof Integer;
    }

    public void show(String end) {
        if (end == null) end = "";
        System.out.print(num + end);
    }

    public String toString() {
        return num + "";
    }

}
