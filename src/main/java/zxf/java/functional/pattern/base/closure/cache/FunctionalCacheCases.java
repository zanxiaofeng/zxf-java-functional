package zxf.java.functional.pattern.base.closure.cache;

import zxf.java.functional.pattern.base.TriFunction;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FunctionalCacheCases {
    public static void main(String[] args) {
        use_case1();
        use_case2();
        use_case3();
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

    public static void use_case3() {
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(y -> {
                Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(z -> {
                    System.out.println("calculate result for " + x + ", " + y + ", " + z + " is " + realCalculate3(x, y, z));
                });
            });
        });

        TriFunction<Integer, Integer, Integer, Integer> cachedCalculate3 = FunctionalCache.cachedFunction(FunctionalCacheCases::realCalculate3);
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(y -> {
                Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(z -> {
                    System.out.println("calculate result by cache for " + x + ", " + y + ", " + z + " is " + cachedCalculate3.apply(x, y, z));
                });
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

    public static Integer realCalculate3(Integer x, Integer y, Integer z) {
        System.out.println("real calculate for " + x + ", " + y + ", " + z);
        return IntStream.concat(IntStream.concat(IntStream.range(1, x + 1), IntStream.range(1, y + 1)), IntStream.range(1, z + 1)).sum();
    }
}
