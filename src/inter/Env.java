package inter;

import lexer.Word;

import java.util.Hashtable;

public class Env {
    private final Hashtable<String, Id> hashtable = new Hashtable<>();
    public Env nextEnv;

    public Env(Env oldEnv) {
        nextEnv = oldEnv;
    }

    void put(String key, Id id) {
        hashtable.put(key, id);
    }

    Id get(String key) {
        Id id = hashtable.get(key);
        Env env = this;
        while (id == null) {
            if (env.nextEnv == null) break;
            env = env.nextEnv;
            id = env.hashtable.get(key);
        }
        return id;
    }
}
