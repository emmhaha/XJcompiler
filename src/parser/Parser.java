package parser;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

import java.io.IOException;
import java.util.*;

public class Parser {

    private Lexer lexer;
    private Token token;
    private LinkedList<Integer> stateStack = new LinkedList<>();
    private ArrayList<ItemSet> stateSet = new ArrayList<>();
    private ItemSet grammar = new ItemSet();
    private ArrayList<String> symbols = new ArrayList<>();
    private Hashtable<Integer, Hashtable<String, String>> Action = new Hashtable<>();
    private Hashtable<Integer, Hashtable<String, String>> Goto = new Hashtable<>();

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        init();
        //showState();
        //showTable(9);
        analyze();

//        while ((token = lexer.getToken()) != null) {
//            token.show("  ");
//        }
    }

    private void init() {

//        String[] G = {
//                "program -> block", "block -> { decls stmts }", "decls -> decls decl",
//                "decls -> NULL", "decl -> type ID ;", "type -> type [ NUM ]",
//                "type -> BASIC", "stmts -> stmts stmt", "stmts -> NULL",
//                "stmt -> loc = bool ;", "stmt -> IF ( bool ) stmt",
//                "stmt -> IF ( bool ) stmt ELSE stmt", "stmt -> WHILE ( bool ) stmt",
//                "stmt -> DO stmt WHILE ( bool ) ;", "stmt -> BREAK ;", "stmt -> block",
//                "loc -> loc [ bool ]", "loc -> ID", "bool -> bool || join",
//                "bool -> join", "join -> join && equality", "join -> equality",
//                "equality -> equality == rel", "equality -> equality != rel",
//                "equality -> rel", "rel -> expr < expr", "rel -> expr <= expr",
//                "rel -> expr >= expr", "rel -> expr > expr", "rel -> expr",
//                "expr -> expr + term", "expr -> expr - term", "expr -> term",
//                "term -> term * unary", "term -> term / unary", "term -> unary",
//                "unary -> ! unary", "unary -> - unary", "unary -> factor",
//                "factor -> ( bool )", "factor -> loc", "factor -> NUM",
//                "factor -> REAL", "factor -> TRUE", "factor -> FALSE"
//        };

        String[] G = {
                "e -> e + t", "e -> t", "t -> t * f", "t -> f",
                "f -> ( e )", "f -> NUM"
        };

//        String[] G = {
//                "s -> c c", "c -> C c", "c -> D"
//        };

        for (int i = 0; i < G.length; i++) {
            String[] g = G[i].split(" -> ");
            Item item = new Item(g[0], g[1], null, i + 1);
            grammar.add(item);

            if (!symbols.contains(item.key)) symbols.add(item.key);
            item.value.forEach(s -> {
                if (!symbols.contains(s)) symbols.add(s);
            });
        }
        String[] first_item = G[0].split(" -> ");
        items(new Item(first_item[0] + "'", ". " + first_item[0], "$"));
    }

    private Token move() throws IOException {
        return token = lexer.getToken();
    }

    private boolean match(String tag) throws IOException {
        if (token != null && tag.equals(this.token.tag)) {
            move();
            return true;
        }
        return false;
    }

    private void analyze() throws IOException {
        stateStack.add(0);
        Token a = move();
        while (a != null) {
            int topState = stateStack.getLast();
            String action = getTable(Action, topState, a.tag);
            if (action == null) {
                System.out.printf("在第%d行附近可能有错\n", lexer.lines);
                return;
            }
            else if (action.charAt(0) == 's') {
                int state = Integer.parseInt(action.substring(1));
                stateStack.addLast(state);
                a = move();
            }
            else if (action.charAt(0) == 'r') {
                int id = Integer.parseInt(action.substring(1));
                Item item = grammar.getItem(id);
                int size = item.value.size();
                for (int i = 0; i < size; i++) {
                    stateStack.removeLast();
                }
                topState = stateStack.getLast();
                String state = getTable(Goto, topState, item.key);
                assert state != null;
                stateStack.addLast(Integer.parseInt(state));
            }
            else break;
        }
        System.out.println("语法正确！");
    }

    private void setGoto (int i, String s, String value) {
        setTable(i, s, value, Goto);
    }

    private void setAction(int id, String s, String value) {
        setTable(id, s, value, Action);
    }

    private void setTable(int i, String s, String value, Hashtable<Integer, Hashtable<String, String>> table) {
        Hashtable<String, String> hashTemp = new Hashtable<>();
        if (table.get(i) != null) hashTemp = table.get(i);
        hashTemp.put(s, value);
        table.put(i, hashTemp);
    }

    private String getTable(Hashtable<Integer, Hashtable<String, String>> table, int id, String s) {
        if (table.get(id) == null) return null;
        return table.get(id).get(s);
    }

    private void items(Item initial_item) {
        int id = 0;
        ItemSet itemSet = new ItemSet(0);
        itemSet.add(initial_item);
        stateSet.add(CLOSURE(itemSet));
        for (int i = 0; i < stateSet.size(); i++) {
            ItemSet j = stateSet.get(i);
            for (String s : symbols) {
                ItemSet newState = GOTO(j, s);
                if (newState.size() > 0 ) {
                    if (!stateSet.contains(newState)){
                        newState.setID(++id);
                        stateSet.add(newState);
                    }
                    setGoto(j.ID, s, stateSet.indexOf(newState) + "");    // GOTO表构造
                    j.forEach(item -> {              // ACTION表构造条件第一条
                        if (!Character.isLowerCase(s.charAt(0)) && s.equals(item.nextToPoint(1))) {
                            setAction(j.ID, s, "s" + stateSet.indexOf(newState) + "");
                        }
                    });
                }
            }
        }
        for(ItemSet is : stateSet) {    // ACTION表构造条件第二、三条
            for (Item i : is) {
                if (i.equals(initial_item.movePoint())) setAction(is.ID, i.f_symbol, "acc");
                if (!i.key.equals(initial_item.key) && i.nextToPoint(1) == null)
                    setAction(is.ID, i.f_symbol, "r" + grammar.getItem(i).id);
            }
        }
    }

    private ItemSet GOTO(ItemSet I, String X) {
        ItemSet J = new ItemSet();
        for (Item item : I) {
            if (item.nextToPoint(1) == null) continue;
            if (item.nextToPoint(1).equals(X)) J.add(item.movePoint());
        }
        return CLOSURE(J);
    }

    private ItemSet CLOSURE(ItemSet I) {
        ArrayList<Item> itemList = I.toList();
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            for (Item g : grammar.getItem(item.nextToPoint(1))) {
                for (String b : FIRST(item.nextToPoint(2), item.f_symbol)) {
                    Item newItem = new Item(item.nextToPoint(1), ". " + g.valueToString(), b);
                    if (!itemList.contains(newItem)) itemList.add(newItem);
                }
            }
        }
        I.addAll(itemList);
        return I;
    }

    private HashSet<String> FIRST(String ...keys) {
        HashSet<String> hashSet = new HashSet<>();
        for (String i : keys) {
            if (i == null) continue;
            HashSet<String> tempSet = FIRST(i);
            tempSet.forEach(s -> {                      // 加入所有不为空的
                if (!s.equals("NULL")) hashSet.add(s);
            });
            if (!tempSet.contains("NULL")) break;
            if (keys[keys.length - 1].equals(i)) hashSet.add("NULL");
        }
        return hashSet;
    }

    private HashSet<String> FIRST(String key) {
        HashSet<String> hashSet = new HashSet<>();
        if (!Character.isLowerCase(key.charAt(0))) {   // 终结符号
            hashSet.add(key);
            return hashSet;
        }
        for (Item item : grammar.getItem(key)) {    // 文法中每个非终结符号为key的产生式
            for (String s : item.value) {           // 产生式体中的每个符号
                if (key.equals(s)) {    // 如果出现左递归就判断是否能推出空
                    if (!canDeriveNull(s)) break;   // 不能推出空就跳过当前产生式，通过上层循环中的同名非递归产生式来求FIRST(s)
                    else continue;    // 若能推出空就跳过当前文法符号，继续求此产生式的下一符号的FIRST集
                }
                hashSet.addAll(FIRST(s));
                if (!canDeriveNull(s)) break;
            }
        }
        return hashSet;
    }

    private boolean canDeriveNull(String key) {
        for (Item item : grammar.getItem(key)) {
            for (String i : item.value) {
                if (key.equals(i)) break;          // 出现左递归说明当前产生式已经推不出结果，应该从其他同名产生式得出结果
                                                    // 所以应当跳过当前产生式
                if (i.equals("NULL")) return true;
                if (!Character.isLowerCase(key.charAt(0))) break;    // 终结符号
                if (!canDeriveNull(i)) break;
                if (item.value.indexOf(i) == item.value.size()-1) return true;    // 所有非终结符号都能推出空
            }
        }
        return false;
    }

    public void showState() {
        System.out.print("\n\n状态集：\n");
        for (ItemSet i : stateSet) {
            i.show();
        }
    }

    public void showTable(int width) {
        System.out.print("\n\n语法分析表：\n");
        printSpace(width - 1);
        ArrayList<String> symbols_goto = new ArrayList<>(symbols);
        symbols.add("$");
        symbols.removeIf(s -> Character.isLowerCase(s.charAt(0)));
        symbols_goto.removeIf(s -> !Character.isLowerCase(s.charAt(0)));
        for (String s : symbols) {
            System.out.print(s);
            printSpace(width - s.length());
        }
        System.out.print("|   ");
        for (String s : symbols_goto) {
            System.out.print(s);
            printSpace(width - s.length());
        }
        for (int i = 0; i < stateSet.size(); i++) {
            System.out.print("\n" + i);
            printSpace(width - String.valueOf(i).length() - 1);
            Hashtable row_action = Action.get(i);
            Hashtable row_goto = Goto.get(i);
            if (row_action != null) {
                for (String s : symbols) {
                    if (row_action.get(s) == null) printSpace(width);
                    else {
                        String val = (String) row_action.get(s);
                        System.out.print(val);
                        printSpace(width - val.length());
                    }
                }
                System.out.print("|   ");
            }
            else {
                printSpace(symbols.size() * width);
                System.out.print("|   ");
            }
            if (row_goto != null)
                for (String s : symbols_goto) {
                    if (row_goto.get(s) == null) printSpace(width);
                    else {
                        String val = (String) row_goto.get(s);
                        System.out.print(val);
                        printSpace(width - val.length());
                    }
                }
        }
    }

    private void printSpace(int num) {
        for (int i = 0; i < num; i++)
            System.out.print(" ");
    }
}
