package lexer;

public class Imm<T> extends Token {

    private T num;

    Imm(T num) {
        super(Tag.IMM);
        this.num = num;
    }

    public void show(String end) {
        if (end == null) end = "";
        System.out.print(num + end);
    }

    public String toString() {
        return num + "";
    }

}
