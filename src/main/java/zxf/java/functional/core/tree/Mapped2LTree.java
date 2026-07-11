package zxf.java.functional.core.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维键值容器：用「嵌套 {@link Map}」模拟一棵二维键树（并非真正树形节点结构）。
 *
 * <p>对应 Readme 中「用嵌套 Map 模拟多维键树」的概念。所谓「Tree」只是形象说法：
 * 外层 Map 的 key 是第一维 T，内层 Map 的 key 是第二维 U，value 是 V。
 * 借助 {@code putIfAbsent + getOrDefault} 实现按 (T, U) 二元键读写单一 V 值。</p>
 *
 * <p>命名说明：类名沿用了项目历史命名，但请勿误解为树形遍历结构——
 * 它本质上是 {@code Map<T, Map<U, V>>} 的薄封装。</p>
 */
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
