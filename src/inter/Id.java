package inter;

import lexer.Temp;
import lexer.Type;
import lexer.Word;

public class Id extends Expr {
    public int offset;

    Id(Word w, Type type, int offset) {
        super(w, type, false);
        this.offset = offset;
    }

    public String toString() {
        return token.toString();
    }
}
