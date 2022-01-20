package zxf.java.functional.pattern.base.closure.cache;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FunctionalCacheCases {
    public static void main(String[] args) {


    }

    public void use_case1() {
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).map(FunctionalCacheCases::realCalculate).forEach(x -> {
            System.out.println(x);
        });

        Function<Integer, Integer> cachedCalculate = FunctionalCache.cachedFunction(FunctionalCacheCases::realCalculate);
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).map(cachedCalculate).forEach(x -> {
            System.out.println(x);
        });
    }

    public void use_case2() {
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(y -> {
                System.out.println(realCalculate2(x, y));
            });
        });

        BiFunction<Integer, Integer, Integer> cachedCalculate2 = FunctionalCache.cachedFunction(FunctionalCacheCases::realCalculate2);
        Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(x -> {
            Arrays.stream(new Integer[]{10, 20, 30, 10, 20, 30}).forEach(y -> {
                System.out.println(cachedCalculate2.apply(x, y));
            });
        });
    }

    public static Integer realCalculate(Integer x) {
        System.out.println("calculate for " + x);
        return IntStream.range(1, x).sum();
    }

    public static Integer realCalculate2(Integer x, Integer y) {
        System.out.println("calculate for " + x + "," + y);
        return IntStream.concat(IntStream.range(1, x), IntStream.range(1, y)).sum();
    }
}
