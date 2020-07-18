package lexer;

public class Imm<T> extends Token {

    private final T num;
    public Type type;

    public Imm(T num) {
        super(Tag.IMM);
        this.num = num;
        type = getType();
        if (type == null) {
            if (isInteger()) type = Type.Int;
            else type = Type.Float;
        }
    }

    private Type getType() {
        if (num instanceof Imm) return ((Imm) num).type;
        else return null;
    }

    private boolean isInteger() {
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
