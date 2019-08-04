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
        Env env = this;
        while (id == null) {
            if (env.nextEnv == null) break;
            env = env.nextEnv;
            id = env.hashtable.get(w);
        }
        return id;
    }
}
