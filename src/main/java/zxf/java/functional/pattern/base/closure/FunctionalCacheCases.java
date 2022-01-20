package zxf.java.functional.pattern.base.closure;

import java.util.function.Function;

public class FunctionalCacheCases {
    public static void main(String[] args) {
        Function<Integer, Integer> cachedCalculate = FunctionalCache.cachedFunction(FunctionalCacheCases::realCalculate);
        cachedCalculate.apply(100);
    }

    public static Integer realCalculate(Integer x) {
        return x + 2;
    }
}
