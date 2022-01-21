package zxf.java.functional.pattern.base.closure.cache;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FunctionalCacheCases {
    public static void main(String[] args) {
        use_case1();
        use_case2();
    }

    public static void use_case1() {
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            System.out.println("calculate result for " + x + " is " + realCalculate(x));
        });

        Function<Integer, Integer> cachedCalculate = FunctionalCache.cachedFunction(FunctionalCacheCases::realCalculate);
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            System.out.println("calculate result by cache for " + x + " is " + cachedCalculate.apply(x));
        });
    }

    public static void use_case2() {
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(y -> {
                System.out.println("calculate result for " + x + ", " + y + " is " + realCalculate2(x, y));
            });
        });

        BiFunction<Integer, Integer, Integer> cachedCalculate2 = FunctionalCache.cachedFunction(FunctionalCacheCases::realCalculate2);
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(y -> {
                System.out.println("calculate result by cache for " + x + ", " + y + " is " + cachedCalculate2.apply(x, y));
            });
        });
    }

    public static Integer realCalculate(Integer x) {
        System.out.println("real calculate for " + x);
        return IntStream.range(1, x + 1).sum();
    }

    public static Integer realCalculate2(Integer x, Integer y) {
        System.out.println("real calculate for " + x + ", " + y);
        return IntStream.concat(IntStream.range(1, x + 1), IntStream.range(1, y + 1)).sum();
    }
}
