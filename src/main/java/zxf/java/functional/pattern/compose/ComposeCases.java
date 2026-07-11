package zxf.java.functional.pattern.compose;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 函数组合演示，对应 Readme「函数式的基本模式 - 组合」。
 * 三种组合形态：
 *   - 静态组合：硬编码常量函数（编译期固定）；
 *   - 半动态组合：用 andThen/and/or 串联已有静态方法；
 *   - 动态组合：工厂方法接收函数入参，运行时返回组合后的新函数。
 */
public class ComposeCases {

    public static void main(String[] args) {
        use_case1();
        use_case2();
        use_case3();
        use_case4();
    }

    // use_case1: 半动态组合 —— andThen 串联已存在的静态方法 step1 -> step2
    public static void use_case1() {
        System.out.println("\n#case 1: 半动态组合（andThen 串联 step1 -> step2）");
        Function<Integer, String> function =
                ((Function<Integer, Integer>) ComposeCases::step1).andThen(ComposeCases::step2);
        System.out.println("step1.andThen(step2).apply(2) = " + function.apply(2));
    }

    // use_case2: 半动态组合 —— Predicate and/or 组合多个条件
    public static void use_case2() {
        System.out.println("\n#case 2: 半动态组合（Predicate and/or 组合多条件：偶数 且 (3的倍数 或 ==10)）");
        Predicate<Integer> newFilter =
                ((Predicate<Integer>) ComposeCases::condition1)
                        .and(ComposeCases::condition2)
                        .or(ComposeCases::condition3);
        Arrays.stream(new Integer[]{2, 4, 6, 8, 10, 12, 20, 30, 32, 40})
                .filter(newFilter)
                .forEach(x -> System.out.println("  命中: " + x));
    }

    // use_case3: 静态组合 —— 直接硬编码定义一个新的组合函数常量（编译期固定，不可变）
    public static final Function<Integer, String> STATIC_COMPOSED =
            ((Function<Integer, Integer>) ComposeCases::step1)
                    .andThen(x -> x * 10)
                    .andThen(ComposeCases::step2);

    public static void use_case3() {
        System.out.println("\n#case 3: 静态组合（硬编码常量函数，编译期固定）");
        System.out.println("STATIC_COMPOSED.apply(3) = " + STATIC_COMPOSED.apply(3));
    }

    // use_case4: 动态组合 —— 工厂方法接收函数入参，运行时返回组合后的新函数
    public static Function<Integer, String> composeFactory(Function<Integer, Integer> f,
                                                           Function<Integer, Integer> g,
                                                           Function<Integer, String> formatter) {
        return f.andThen(g).andThen(formatter);
    }

    public static void use_case4() {
        System.out.println("\n#case 4: 动态组合（工厂方法接收函数入参，运行时组合）");
        Function<Integer, String> dyn = composeFactory(
                x -> x + 1,
                x -> x * x,
                i -> "结果=" + i);
        System.out.println("composeFactory(x+1, x*x, fmt).apply(4) = " + dyn.apply(4));

        // 换一组函数即得不同行为，无需新建类
        Function<Integer, String> dyn2 = composeFactory(
                x -> x - 1,
                x -> x * 2,
                i -> "value=" + i);
        System.out.println("composeFactory(x-1, x*2, fmt).apply(4) = " + dyn2.apply(4));
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
