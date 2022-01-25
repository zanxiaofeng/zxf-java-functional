package zxf.java.functional.pattern.base.closure.cache;

import zxf.java.functional.pattern.base.TriFunction;
import zxf.java.functional.pattern.base.closure.cache.tree.Mapped2LTree;
import zxf.java.functional.pattern.base.closure.cache.tree.Mapped3LTree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionalCache {
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
        return (t, u, p) -> {
            if (cache.contains(t, u, p)) {
                return cache.get(t, u, p);
            }

            R r = realFunction.apply(t, u, p);
            cache.put(t, u, p, r);
            return r;
        };
    }
}
