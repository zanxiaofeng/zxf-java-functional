package zxf.java.functional.pattern.base.closure;

import java.util.HashMap;
import java.util.Map;

public class OOPCache {
    private Map<Integer, Integer> cache = new HashMap<>();

    public Integer cachedCalculate(Integer x) {
        if (cache.containsKey(x)) {
            return cache.get(x);
        }
        Integer y = realCalculate(x);
        cache.put(x, y);
        return y;
    }

    public Integer realCalculate(Integer x) {
        return x + 2;
    }
}
