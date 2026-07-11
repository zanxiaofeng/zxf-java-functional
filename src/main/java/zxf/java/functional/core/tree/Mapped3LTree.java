package zxf.java.functional.core.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 三维键值容器：用「两层嵌套 {@link Map}」模拟一棵三维键树（并非真正树形节点结构）。
 *
 * <p>对应 Readme 中「用嵌套 Map 模拟多维键树」的概念。按 (T, U, P) 三元键读写 V：
 * 外层 Map key=T，中层 key=U，内层 key=P，value=V。</p>
 *
 * <p>命名说明：类名沿用了项目历史命名，本质上是
 * {@code Map<T, Map<U, Map<P, V>>>} 的薄封装，不支持树形遍历。</p>
 */
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
