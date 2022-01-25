package zxf.java.functional.pattern.base.closure.cache.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Mapped2LTree<T, U, V> {
    private Map<T, Map<U, V>> tree = new HashMap<>();

    public boolean contains(T t, U u) {
        return tree.getOrDefault(t, Collections.emptyMap()).containsKey(u);
    }

    public V get(T t, U u) {
        return tree.getOrDefault(t, Collections.emptyMap()).get(u);
    }

    public void put(T t, U u, V v) {
        tree.putIfAbsent(t, new HashMap<>());
        tree.get(t).put(u, v);
    }
}
