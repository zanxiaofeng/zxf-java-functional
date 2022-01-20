package zxf.java.functional.pattern.base.closure.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionalCache {
    public static <T, R> Function<T, R> cachedFunction(Function<T, R> realFunction) {
        final Map<T, R> cache = new HashMap<>();
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
        final Map<T, Map<U, R>> cache = new HashMap<>();
        return (t, u) -> {
            Map<U, R> innerCache = cache.putIfAbsent(t, new HashMap<>());
            if (innerCache.containsKey(u)) {
                return innerCache.get(u);
            }

            R r = realFunction.apply(t, u);
            innerCache.put(u, r);
            return r;
        };
    }
}
