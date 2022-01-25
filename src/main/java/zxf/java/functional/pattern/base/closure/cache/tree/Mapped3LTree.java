package zxf.java.functional.pattern.base.closure.cache.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Mapped3LTree<T, U, P, V> {
    private Map<T, Map<U, Map<P, V>>> tree = new HashMap<>();

    public boolean contains(T t, U u, P p) {
        return tree.getOrDefault(t, Collections.emptyMap())
                .getOrDefault(u, Collections.emptyMap())
                .containsKey(p);
    }

    public V get(T t, U u, P p) {
        return tree.getOrDefault(t, Collections.emptyMap())
                .getOrDefault(u, Collections.emptyMap())
                .get(p);
    }

    public void put(T t, U u, P p, V v) {
        tree.putIfAbsent(t, new HashMap<>());
        tree.get(t).putIfAbsent(u, new HashMap<>());
        tree.get(t).get(u).put(p, v);
    }
}