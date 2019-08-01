package inter;

import lexer.*;
import parser.Parser;

import java.util.Hashtable;

public class FunctionSet {
    private static int used = 0;
    private String firstLex;
    private Hashtable<Integer, Unit> semanticStack;
    private Parser parser;
    private Env env;
    private int top;
    private Unit unit_top;
    private Unit unit_2;
    private Unit unit_3;
    private Unit unit_4;
    private Token symbol_top;
    private Token symbol_2;
    private Token symbol_3;

    public FunctionSet(Parser parser, String firstLex) {
        this.top = parser.symbolStack.size()-1;
        this.parser = parser;
        this.firstLex = firstLex;
        this.env = parser.env;
        this.semanticStack = parser.semanticStack;
        this.unit_top = semanticStack.get(top);
        this.unit_2 = semanticStack.get(top - 1);
        this.unit_3 = semanticStack.get(top - 2);
        this.unit_4 = semanticStack.get(top - 3);
        this.symbol_top = parser.symbolStack.get(top);
        if (top - 1 >= 0) this.symbol_2 = parser.symbolStack.get(top - 1);
        if (top - 2 >= 0) this.symbol_3 = parser.symbolStack.get(top - 2);
    }

    public void program() {
        if (unit_top.codes.contains("break")) parser.error("break语句只能出现在循环语句内！");
    }

    public void block() {
        Unit unit = new Unit();
        unit.addCodes(unit_2.codes);
        semanticStack.put(top - 3, unit);
    }

    public void decls() {
        if (firstLex.equals("decls")) {
            Unit unit = new Unit();
            //unit.isCode = false;
            semanticStack.put(top - 2, unit);
        }
    }

    public void decl() {
        used += unit_3.getType().width;
        Id id = new Id((Word) symbol_2, unit_3.getType(), used);
        id.currentTemp = new Temp(id.type);
        env.put((Word) symbol_2, id);
        Unit unit = new Unit();
        semanticStack.put(top - 2, unit);
    }

    public void type() {
        if (firstLex.equals("BASIC")) semanticStack.put(top, new Expr(symbol_top, (Type) symbol_top, false));
        else {
            if (unit_4.isCode) parser.error("暂不支持多维数组");
            else {
                Array array = new Array(Integer.parseInt(symbol_2.toString()), unit_4.getType());
                Expr expr = new Expr(symbol_2, array, false);
                expr.isArray = true;
                semanticStack.put(top - 3, expr);
            }
        }
    }

    public void stmts() {
        if (firstLex.equals("stmts")) {
            //if ((unit_2 != null && unit_2.codes.contains("break")) || unit_top.codes.contains("break")) parser.error("break语句只能出现在循环语句内！");
            Unit unit = new Unit();
            if (unit_2 != null) unit.addCodes(unit_2.codes);
            unit.addCodes(unit_top.codes);
            semanticStack.put(top - 1, unit);
        }
    }

    public void stmt() {
        Unit unit;
        String newLabel;
        switch (firstLex) {
            case "loc":
                unit = new Unit();
                if (unit_2.isCode) {          // bool是算式
                    if (unit_4.isArray) {      // loc是数组
                        String s = unit_4.codes.get(unit_4.codes.size() - 1);
                        unit_4.codes.remove(unit_4.codes.size() - 1);
                        unit.addCodes(unit_4.codes);
                        unit.addCodes(unit_2.codes);
                        unit.addCode(s + " = " + unit_2.currentTemp);
                    }
                    else {                    // loc是id
                        unit.addCodes(unit_2.codes);
                        unit.currentTemp = unit_4.currentTemp;
                        unit.addCode(unit_4.currentTemp + " = " + unit_2.currentTemp);
                    }
                }
                else {                       // bool是num
                    if (unit_4.isArray) {     // loc是数组
                        String s = unit_4.codes.get(unit_4.codes.size() - 1);
                        unit_4.codes.remove(unit_4.codes.size() - 1);
                        unit.addCodes(unit_4.codes);
                        unit.addCode(s + " = " + ((Expr) unit_2).token);
                    }
                    else {                   // loc是id
                        unit.currentTemp = unit_4.currentTemp;
                        unit.addCode(unit_4.currentTemp + " = " + ((Expr) unit_2).token);
                    }
                }
                semanticStack.put(top - 3, unit);
                break;
            case "IF":
                newLabel = Unit.newLabel();
                unit = new Unit();
                if (symbol_2.tag.equals(")")) {
                    unit.addCodes(unit_3.codes);
                    unit.addCode("if " + unit_3.currentTemp + " is false goto " + newLabel);
                    unit.addCodes(unit_top.codes);
                    unit.addCode(newLabel + ":");
                    semanticStack.put(top - 4, unit);
                }
                else {
                    String newLabel2 = Unit.newLabel();
                    unit.addCodes(semanticStack.get(top - 4).codes);
                    unit.addCode("if " + semanticStack.get(top - 4).currentTemp + " is false goto " + newLabel);
                    unit.addCodes(unit_3.codes);
                    unit.addCode("goto " + newLabel2);
                    unit.addCode(newLabel + ":");
                    unit.addCodes(unit_top.codes);
                    unit.addCode(newLabel2 + ":");
                    semanticStack.put(top - 4, unit);
                }
                break;
            case "WHILE":
                newLabel = Unit.newLabel();
                String newLabel2 = Unit.newLabel();
                unit = new Unit();
                unit.addCode(newLabel + ":");
                unit.addCodes(unit_3.codes);
                if (!unit_3.isCode && ((Expr) unit_3).token.tag.equals(Tag.FALSE)) unit.addCode("goto " + newLabel2);
                else if (!unit_3.isCode && ((Expr) unit_3).token.tag.equals(Tag.TRUE)) {}
                else unit.addCode("if " + unit_3.currentTemp + " is false goto " + newLabel2);

                if (unit_top.codes.contains("break")) {
                    int i = unit_top.codes.indexOf("break");
                    unit_top.codes.removeIf(s1 -> s1.equals("break"));
                    unit_top.codes.add(i, "goto " + newLabel2);
                }
                unit.addCodes(unit_top.codes);
                unit.addCode("goto " + newLabel);
                unit.addCode(newLabel2 + ":");
                semanticStack.put(top - 4, unit);
                break;
            case "DO":
                newLabel = Unit.newLabel();
                newLabel2 = Unit.newLabel();
                unit = new Unit();
                unit.addCode(newLabel + ":");
                boolean hasBreak = semanticStack.get(top - 5).codes.contains("break");
                if (hasBreak) {
                    int i = semanticStack.get(top - 5).codes.indexOf("break");
                    semanticStack.get(top - 5).codes.removeIf(s1 -> s1.equals("break"));
                    semanticStack.get(top - 5).codes.add(i, "goto " + newLabel2);
                }
                unit.addCodes(semanticStack.get(top - 5).codes);
                unit.addCodes(unit_3.codes);

                if (!unit_3.isCode && ((Expr) unit_3).token.tag.equals(Tag.FALSE)) {}
                else if (!unit_3.isCode && ((Expr) unit_3).token.tag.equals(Tag.TRUE)) unit.addCode("goto " + newLabel);
                else unit.addCode("if " + unit_3.currentTemp + " goto " + newLabel);

                if (hasBreak) unit.addCode(newLabel2 + ":");
                semanticStack.put(top - 6, unit);
                break;
            case "BREAK":
                semanticStack.put(top - 1, new Unit("break", true));
                break;
            case "block":
                break;
        }
    }

    public void loc() {
        if (firstLex.equals("ID")) {
            Id id = env.get((Word) symbol_top);
            if (id == null) { parser.error("可能原因：标识符 “" + symbol_top + "” 没有预先定义！"); return; }
            semanticStack.put(top, id);
        }
        else {
            if (unit_2.getType() != Type.Bool && unit_2.getType() != Type.Float) {
                Unit unit = new Unit();
                if (!unit_4.isCode) {
                    unit.addCodes(unit_2.codes);
                    unit.addCode((unit_4).toString() + "[" + unit_2.currentTemp + "]");
                    semanticStack.put(top - 3, unit);
                }
                else {
                    parser.error("暂不支持多维数组");
//                    unit.addCodes(unit_4.codes);
//                    Temp newTemp = new Temp(unit_4.getType());
//                    unit.addCode();
                }
            }
            else parser.error("类型错误：数组索引应该为整型！");
        }
    }

    public void bool() {
        subFunction("join", Type.Bool);
    }

    public void join() {
        subFunction("equality", Type.Bool);
    }

    public void equality() {
        subFunction("rel", Type.Bool);
    }

    public void rel() {
        subFunction("", Type.Bool);
    }

    public void expr() {
        if (firstLex.equals("term")) return;
        Type newType = Type.max(unit_top.getType(), unit_3.getType());
        if (newType != null) subFunction("term", newType);
        else parser.error("类型错误：bool类型不能执行算术操作！");
    }

    public void term() {
        if (firstLex.equals("unary")) return;
        Type newType = Type.max(unit_top.getType(), unit_3.getType());
        if (newType != null) subFunction("unary", newType);
        else parser.error("类型错误：bool类型不能执行算术操作！");
    }

    private void subFunction(String name, Type newType) {

        if (!firstLex.equals(name)) {
            Temp newTemp = new Temp(newType);
            if (unit_top.currentTemp != null && unit_3.currentTemp != null) {
                Unit unit = new Unit(unit_top.codes, newTemp);
                unit.addCodes(unit_3.codes);
                unit.addCode(newTemp.toString() + " = " + unit_3.currentTemp + " " + symbol_2 + " " + unit_top.currentTemp);
                semanticStack.put(top - 2, unit);
            }
            else if (unit_top.currentTemp != null) {
                Unit unit = new Unit(unit_top.codes, newTemp);
                unit.addCode(newTemp.toString() + " = " + ((Expr) unit_3).token + " " + symbol_2 + " " + unit_top.currentTemp);
                semanticStack.put(top - 2, unit);
            }
            else if (unit_3.currentTemp != null) {
                Unit unit = new Unit(unit_3.codes, newTemp);
                unit.addCode(newTemp.toString() + " = " + unit_3.currentTemp + " " + symbol_2 + " " + ((Expr) unit_top).token);
                semanticStack.put(top - 2, unit);
            }
            else {
                Unit unit = new Unit(newTemp.toString() + " = " + ((Expr) unit_3).token + " " + symbol_2 + " " + ((Expr) unit_top).token, newTemp);
                semanticStack.put(top - 2, unit);
            }
        }
    }

    public void unary() {
        if (!firstLex.equals("factor")) {
            if (unit_top.getType() == Type.Bool && symbol_2.tag.equals("!")) {
                if (((Expr) unit_top).token == Word.True)
                    semanticStack.put(top - 1, Constant.False);
                else semanticStack.put(top - 1, Constant.True);
            }
            else if ((unit_top.getType() == Type.Int || unit_top.getType() == Type.Float ||
                    unit_top.getType() == Type.Char) && symbol_2.tag.equals("-")) {
                if (unit_top.isCode) {
                    String[] code = ((Expr) unit_top).token.toString().split(" = ");
                    if (code[1].charAt(0) == '-') semanticStack.put(top - 1, new Unit(code[0] + " = " + code[1].substring(1), unit_top.currentTemp));
                    else semanticStack.put(top - 1, new Unit(code[0] + " = -" + code[1], unit_top.currentTemp));
                }
                else {
                    Temp newTemp = new Temp(unit_top.getType());
                    semanticStack.put(top - 1, new Unit(newTemp.toString() + " = " + symbol_2.toString() + ((Expr) unit_top).token, newTemp));
                }
            }
            else if (unit_top.getType() != Type.Bool && symbol_2.tag.equals("!")) parser.error("类型错误：只有bool类型才能使用“!”修饰");
            else if (!Type.isNumeric(unit_top.getType()) && symbol_2.tag.equals("-")) parser.error("类型错误：只有数值类型才能使用“-”修饰");
            else parser.error("类型错误");
        }
    }

    public void factor() {
        if (firstLex.equals("(")) semanticStack.put(top - 2, unit_2);
        else if (firstLex.equals("loc")) {

        }
        else if (symbol_top.tag.equals(Tag.TRUE)) semanticStack.put(top, Constant.True);
        else if (symbol_top.tag.equals(Tag.FALSE)) semanticStack.put(top, Constant.False);
        else semanticStack.put(top, new Constant(new Imm<>(symbol_top)));
    }
}
