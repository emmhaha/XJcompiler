package inter;

import lexer.Imm;
import lexer.Token;
import lexer.Type;
import lexer.Word;

class Constant extends Expr {
    static final Constant
            True = new Constant(Word.True, Type.Bool),
            False = new Constant(Word.False, Type.Bool);

    Constant(Imm imm) {
        super(imm, imm.type, false);
    }

    private Constant(Token token, Type type) {
        super(token, type, false);
    }
}
