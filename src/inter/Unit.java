package inter;

import lexer.Temp;
import lexer.Type;

import java.util.ArrayList;

public class Unit {

    Temp currentTemp;
    private static int label = 0;
    public ArrayList<String> codes = new ArrayList<>();
    boolean isCode = true;
    boolean isArray = false;

    Unit() {}
    Unit(String code, Temp currentTemp) {
        codes.add(code);
        this.currentTemp = currentTemp;
    }
    Unit(ArrayList<String> codes, Temp currentTemp) {
        this.codes.addAll(codes);
        this.currentTemp = currentTemp;
    }
    Unit(String code, boolean isCode) {
        if (isCode) codes.add(code);
        this.isCode = isCode;
    }

    Type getType() {
        if (currentTemp != null) return ((Expr) currentTemp).type;
        try {
            if (((Expr) this).type != null) return ((Expr) this).type;
        } catch (ClassCastException e) {
            return null;
        }
        return null;
    }

    void addCode(String code) {
        codes.add(code);
    }

    void addCodes(ArrayList<String> codes) {
        this.codes.addAll(codes);
    }

    static String newLabel() {
        return "L" + ++label;
    }

    public static void initLabel() {
        label = 0;
    }
}
