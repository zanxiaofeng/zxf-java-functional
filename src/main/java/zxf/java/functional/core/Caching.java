package zxf.java.functional.core;

import zxf.java.functional.core.function.TriFunction;
import zxf.java.functional.core.function.TriPredicate;
import zxf.java.functional.core.tree.Mapped2LTree;
import zxf.java.functional.core.tree.Mapped3LTree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 函数缓存（memoization）演示：用闭包捕获 HashMap，为纯函数透明地加一层缓存。
 *
 * <p><b>演示用途</b>：缓存无界（无淘汰策略）且非线程安全，请勿照抄到生产代码；
 * 生产环境请使用 Guava Cache / Caffeine / ConcurrentHashMap。</p>
 */
public class Caching {
    public static <T, R> Function<T, R> cachedFunction(Function<T, R> realFunction) {
        final Map<T, R> cache = new HashMap<>();
        return cachedFunction(cache, realFunction);
    }

    public static <T, R> Function<T, R> cachedFunction(Map<T, R> cache, Function<T, R> realFunction) {
        return (t) -> {
            if (cache.containsKey(t)) {
                return cache.get(t);
            }
            R r = realFunction.apply(t);
            cache.put(t, r);
            return r;
        };
    }

    public static <T, U, R> BiFunction<T, U, R> cachedFunction(BiFunction<T, U, R> realFunction) {
        final Mapped2LTree<T, U, R> cache = new Mapped2LTree();
        return cachedFunction(cache, realFunction);
    }

    public static <T, U, R> BiFunction<T, U, R> cachedFunction(Mapped2LTree<T, U, R> cache, BiFunction<T, U, R> realFunction) {
        return (t, u) -> {
            if (cache.contains(t, u)) {
                return cache.get(t, u);
            }

            R r = realFunction.apply(t, u);
            cache.put(t, u, r);
            return r;
        };
    }

    public static <T, U, P, R> TriFunction<T, U, P, R> cachedFunction(TriFunction<T, U, P, R> realFunction) {
        final Mapped3LTree<T, U, P, R> cache = new Mapped3LTree();
        return cachedFunction(cache, realFunction);
    }

    public static <T, U, P, R> TriFunction<T, U, P, R> cachedFunction(Mapped3LTree<T, U, P, R> cache, TriFunction<T, U, P, R> realFunction) {
        return (t, u, p) -> {
            if (cache.contains(t, u, p)) {
                return cache.get(t, u, p);
            }

            R r = realFunction.apply(t, u, p);
            cache.put(t, u, p, r);
            return r;
        };
    }

    public static <T> Predicate<T> cachedPredicate(Predicate<T> realPredicate) {
        final Map<T, Boolean> cache = new HashMap<>();
        return cachedPredicate(cache, realPredicate);
    }

    public static <T> Predicate<T> cachedPredicate(Map<T, Boolean> cache, Predicate<T> realPredicate) {
        return (t) -> {
            if (cache.containsKey(t)) {
                return cache.get(t);
            }
            Boolean r = realPredicate.test(t);
            cache.put(t, r);
            return r;
        };
    }

    public static <T, U> BiPredicate<T, U> cachedPredicate(BiPredicate<T, U> realPredicate) {
        final Mapped2LTree<T, U, Boolean> cache = new Mapped2LTree();
        return cachedPredicate(cache, realPredicate);
    }

    public static <T, U> BiPredicate<T, U> cachedPredicate(Mapped2LTree<T, U, Boolean> cache, BiPredicate<T, U> realPredicate) {
        return (t, u) -> {
            if (cache.contains(t, u)) {
                return cache.get(t, u);
            }
            Boolean r = realPredicate.test(t, u);
            cache.put(t, u, r);
            return r;
        };
    }

    public static <T, U, P> TriPredicate<T, U, P> cachedPredicate(TriPredicate<T, U, P> realPredicate) {
        final Mapped3LTree<T, U, P, Boolean> cache = new Mapped3LTree();
        return cachedPredicate(cache, realPredicate);
    }

    public static <T, U, P> TriPredicate<T, U, P> cachedPredicate(Mapped3LTree<T, U, P, Boolean> cache, TriPredicate<T, U, P> realPredicate) {
        return (t, u, p) -> {
            if (cache.contains(t, u, p)) {
                return cache.get(t, u, p);
            }

            Boolean r = realPredicate.test(t, u, p);
            cache.put(t, u, p, r);
            return r;
        };
    }
}
