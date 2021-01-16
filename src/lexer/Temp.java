package lexer;

import inter.Expr;

public class Temp extends Expr {
    private static int count = 0;
    private final int num;

    public Temp(Type type) {
        super(Word.temp, type, true);
        num = ++count;
    }

    public String toString() {
        return "t" + num;
    }

    public static void init() {
        count = 0;
    }
}
