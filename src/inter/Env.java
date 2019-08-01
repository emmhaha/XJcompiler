package inter;

import lexer.Word;

import java.util.Hashtable;

public class Env {
    private Hashtable<Word, Id> hashtable = new Hashtable<>();
    public Env nextEnv;

    public Env(Env oldEnv) {
        nextEnv = oldEnv;
    }

    void put(Word w, Id id) {
        hashtable.put(w, id);
    }

    Id get(Word w) {
        Id id = hashtable.get(w);
        while (id == null) {
            if (nextEnv == null) break;
            Env env = nextEnv;
            id = env.hashtable.get(w);
        }
        return id;
    }
}
