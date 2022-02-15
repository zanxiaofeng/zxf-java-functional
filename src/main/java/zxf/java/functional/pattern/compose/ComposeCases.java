package zxf.java.functional.pattern.compose;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class ComposeCases {

    public static void main(String[] args) {

    }

    public static void use_case1() {
        Function<Integer, String> function = ((Function<Integer, Integer>) ComposeCases::step1).andThen(ComposeCases::step2);
        function.apply(2);
    }

    public static void use_case2() {
        Predicate<Integer> newFilter = ((Predicate<Integer>) ComposeCases::condition1).and(ComposeCases::condition2).or(ComposeCases::condition3);
        Arrays.stream(new Integer[]{2, 4, 6, 8, 10, 12, 20, 30, 32, 40}).filter(newFilter).forEach(x -> {
            System.out.println(x);
        });
    }

    public static boolean condition1(Integer x) {
        return x % 2 == 0;
    }


    public static boolean condition2(Integer x) {
        return x % 3 == 0;
    }

    public static boolean condition3(Integer x) {
        return x == 10;
    }

    public static Integer step1(Integer x) {
        return x + 2;
    }

    public static String step2(Integer x) {
        return x.toString();
    }
}
