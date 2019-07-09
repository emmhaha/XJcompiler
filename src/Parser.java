import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

import java.io.IOException;
import java.util.Vector;

public class Parser {

    private Lexer lexer;
    private Token token;
    private Vector<String> vec = new Vector<>();

    Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        move();
        int ans = E();
        System.out.println("\n= " + ans);
        show();
    }

    Token move() throws IOException {
        return token = lexer.getToken();
    }

    boolean match(String tag) throws IOException {
        if (token != null && tag.equals(this.token.tag)) {
            move();
            return true;
        }
        return false;
    }

    int E() throws IOException {
        vec.add("E");
        return T() + E_();
    }

    int E_() throws IOException {
        vec.add("E'");
        if (match(Tag.ADD)) {
            return T() + E_();
        } else return 0;
    }

    int T() throws IOException {
        vec.add("T");
        return F() * T_();
    }

    int T_() throws IOException {
        vec.add("T'");
        if (match(Tag.MUL)) {
            return F() * T_();
        } else return 1;
    }

    int F() throws IOException {
        vec.add("F");
        Token last_token = token;
        match(Tag.NUM);
        return last_token.value;
    }

    public void show() {
        System.out.print("\n\n语法树：");
        for (String s : vec) {
            System.out.print(s + " ");
        }
    }
}
