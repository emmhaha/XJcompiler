package lexer;

import inter.Expr;

public class Temp extends Expr {
    private static int count = 0;
    private int num = 0;

    public Temp(Type type) {
        super(Word.temp, type, true);
        num = ++count;
    }

    public String toString() {
        return "t" + num;
    }
}
