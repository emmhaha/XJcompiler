package parser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ItemSet implements Iterable<Item> {

    private HashSet<Item> itemSet = new HashSet<>();
    int ID = 0;

    ItemSet() {}

    ItemSet(int id) {
        setID(id);
    }

    void setID(int id) {
        this.ID = id;
    }

    void add(Item item) {
        itemSet.add(item);
    }

    void addAll(ArrayList<Item> itemList) {
        itemSet.addAll(itemList);
    }

    Item getItem(int id) {
        for(Item item : itemSet) {
            if (item.id == id) return item;
        }
        return null;
    }

    ItemSet getItem(String key) {
        ItemSet itemSet = new ItemSet();
        this.itemSet.forEach(item -> {
            if (item.key.equals(key)) itemSet.add(item);
        });
        return itemSet;
    }

    Item getItem(Item item) {
        for (Item i : itemSet) {
            Item j = new Item(i);
            j.value.add(".");
            if (j.key.equals(item.key) && j.value.equals(item.value)) return j;
        }
        return null;
    }

    boolean contains(Item item) {
        return itemSet.contains(item);
    }

    public int hashCode(){
        return itemSet.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        ItemSet itemSet = (ItemSet) o;
        int size = itemSet.size();
        if (this.itemSet.size() != size) return false;
        for (Item item : this.itemSet) {
            for (Item item1 : itemSet) {
                if (item.equals(item1)) size--;
            }
        }
        return size == 0;
    }

    int size() {
        return itemSet.size();
    }

    ArrayList<Item> toList() {
        return new ArrayList<>(itemSet);
    }

    void show() {
        System.out.println(ID + ":");
        for (Item item : itemSet) {
            System.out.println("\t" + item.toString());
        }
    }

    @NotNull
    @Override
    public Iterator<Item> iterator() {
        return itemSet.iterator();
    }
}
