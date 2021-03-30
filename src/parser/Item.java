package parser;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Arrays;

public class Item {

    String key;
    ArrayList<String> value = new ArrayList<>();
    String f_symbol;
    int id = 0;

    Item() {}

    Item(Item item) {
        this.key = item.key;
        this.f_symbol = item.f_symbol;
        this.value = new ArrayList<>(item.value);
        setID(item.id);
    }

    Item(String key, String value, String f_symbol) {
        init(key, value, f_symbol);
    }

    Item(String key, String value, String f_symbol, int id) {
        init(key, value, f_symbol);
        setID(id);
    }

    @JSONField(name = "key")
    public String getKey() {
        return key;
    }

    @JSONField(name = "f_symbol")
    public String getF_symbol() {
        return f_symbol;
    }

    @JSONField(name = "value")
    public ArrayList<String> getValue() {
        return value;
    }

    @JSONField(name = "f_symbol")
    public void setF_symbol(String f_symbol) {
        this.f_symbol = f_symbol;
    }

    @JSONField(name = "key")
    public void setKey(String key) {
        this.key = key;
    }

    @JSONField(name = "value")
    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setID(int id) {
        this.id = id;
    }

    private void init(String key, String value, String f_symbol) {
        this.key = key;
        this.f_symbol = f_symbol;
        this.value.addAll(Arrays.asList(value.split(" ")));
    }

    private int getPoint() {
        return value.indexOf(".");
    }

    String nextToPoint(int offset) {
        if (getPoint() + offset > value.size() - 1) return null;
        return value.get(getPoint() + offset);
    }

    Item movePoint() {    // 获得项的“点”向前移动的结果，并不会改变项的值
        Item item = new Item(this);
        int p = getPoint();
        if (item.value.size() - 1 == p) return this;
        item.value.remove(p);
        item.value.add(p + 1, ".");
        return item;
    }

    public int hashCode(){
        if (f_symbol == null) return key.hashCode() + value.hashCode();
        return key.hashCode() + value.hashCode() + f_symbol.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Item item = (Item) o;
        if (f_symbol == null) return item.key.equals(key) && item.value.equals(value);
        return item.key.equals(key) && item.value.equals(value) && item.f_symbol.equals(f_symbol);
    }

    String valueToString() {
        StringBuilder buffer = new StringBuilder();
        for (String s : value) {
            buffer.append(s);
            buffer.append(" ");
        }
        return buffer.toString();
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(key + " -> ");
        for (String i : value) {
            buffer.append(i);
            buffer.append(" ");
        }
        buffer.append(", ");
        buffer.append(f_symbol);
        return buffer.toString();
    }
}
