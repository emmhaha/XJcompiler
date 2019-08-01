package inter;

import lexer.Token;
import lexer.Type;

public class Expr extends Unit {
    Token token;
    Type type;

    public Expr(Token token, Type type, Boolean isCode) {
        super(token.toString(), isCode);
        this.token = token;
        this.type = type;
    }
}
