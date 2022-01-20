package zxf.java.functional.pattern.base.closure;

import java.util.HashMap;
import java.util.Map;
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
}
