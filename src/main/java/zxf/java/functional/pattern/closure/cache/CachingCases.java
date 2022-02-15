package zxf.java.functional.pattern.closure.cache;

import zxf.java.functional.pattern.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class CachingCases {
    public static void main(String[] args) {
        use_case1();
        use_case2();
        use_case3();
    }

    public static void use_case1() {
        System.out.println("\n#case 1: ");
        System.out.println("calculate result for 10 is " + realCalculate(10));
        System.out.println("calculate result for 10 is " + realCalculate(10));
        System.out.println("calculate result for 10 is " + realCalculate(10));
        System.out.println("*");
        Function<Integer, Integer> cachedCalculate = Caching.cachedFunction(CachingCases::realCalculate);
        System.out.println("calculate result by cache for 10 is " + cachedCalculate.apply(10));
        System.out.println("calculate result by cache for 10 is " + cachedCalculate.apply(10));
        System.out.println("calculate result by cache for 11 is " + cachedCalculate.apply(11));
        System.out.println("calculate result by cache for 11 is " + cachedCalculate.apply(11));
    }

    public static void use_case2() {
        System.out.println("\n#case 2: ");
        System.out.println("calculate result for 10, 10 is " + realCalculate2(10, 10));
        System.out.println("calculate result for 10, 10 is " + realCalculate2(10, 10));
        System.out.println("calculate result for 10, 10 is " + realCalculate2(10, 10));
        System.out.println("*");
        BiFunction<Integer, Integer, Integer> cachedCalculate2 = Caching.cachedFunction(CachingCases::realCalculate2);
        System.out.println("calculate result by cache for 10, 10 is " + cachedCalculate2.apply(10, 10));
        System.out.println("calculate result by cache for 10, 10 is " + cachedCalculate2.apply(10, 10));
        System.out.println("calculate result by cache for 11, 11 is " + cachedCalculate2.apply(11, 11));
        System.out.println("calculate result by cache for 11, 11 is " + cachedCalculate2.apply(11, 11));
    }

    public static void use_case3() {
        System.out.println("\n#case 3: ");
        System.out.println("calculate result for 10, 10, 10 is " + realCalculate3(10, 10, 10));
        System.out.println("calculate result for 10, 10, 10 is " + realCalculate3(10, 10, 10));
        System.out.println("calculate result for 10, 10, 10 is " + realCalculate3(10, 10, 10));
        System.out.println("*");
        TriFunction<Integer, Integer, Integer, Integer> cachedCalculate3 = Caching.cachedFunction(CachingCases::realCalculate3);
        System.out.println("calculate result by cache for 10, 10, 10 is " + cachedCalculate3.apply(10, 10, 10));
        System.out.println("calculate result by cache for 10, 10, 10 is " + cachedCalculate3.apply(10, 10, 10));
        System.out.println("calculate result by cache for 11, 11, 11 is " + cachedCalculate3.apply(11, 11, 11));
        System.out.println("calculate result by cache for 11, 11, 11 is " + cachedCalculate3.apply(11, 11, 11));
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
