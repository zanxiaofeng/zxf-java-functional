package zxf.java.functional.function;

import zxf.java.functional.function.check.product.model.Product;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

// 对应 Readme「FunctionalInterface」表格中 9 个函数式接口的最小 demo。
// 现有 check/ 包只覆盖了 Predicate/Consumer/BiConsumer，这里补全其余 6 个，并为每个接口至少展示一个默认/静态方法。
public class FunctionalInterfaceCases {
    public static void main(String[] args) {
        use_case1_Supplier();
        use_case2_Consumer();
        use_case3_BiConsumer();
        use_case4_Function();
        use_case5_BiFunction();
        use_case6_UnaryOperator();
        use_case7_BinaryOperator();
        use_case8_Predicate();
        use_case9_BiPredicate();
    }

    // Supplier<T>：无参，返回 T。方法：get。
    // 注意：Supplier 没有默认方法（Readme 表格 Others 列标注 N/A）。
    public static void use_case1_Supplier() {
        System.out.println("use_case1 Supplier：() -> new Product()");
        Supplier<Product> supplier = () -> new Product();
        Product product = supplier.get();
        System.out.println("  supplier.get() = " + product);
    }

    // Consumer<T>：消费 T，无返回。默认方法 andThen 实现串行消费。
    public static void use_case2_Consumer() {
        System.out.println("use_case2 Consumer：默认方法 andThen");
        Consumer<String> print = System.out::println;
        Consumer<String> printLen = s -> System.out.println("  长度=" + s.length());
        Consumer<String> combined = print.andThen(printLen);
        combined.accept("hello");
    }

    // BiConsumer<T,U>：消费 T、U。默认方法 andThen 串行执行。
    public static void use_case3_BiConsumer() {
        System.out.println("use_case3 BiConsumer：默认方法 andThen");
        BiConsumer<String, Integer> printKv = (k, v) -> System.out.println("  " + k + "=" + v);
        BiConsumer<String, Integer> printNext = (k, v) -> System.out.println("  " + k + "+1=" + (v + 1));
        BiConsumer<String, Integer> combined = printKv.andThen(printNext);
        combined.accept("count", 10);
    }

    // Function<T,R>：T -> R。默认方法 andThen / compose；静态方法 identity。
    public static void use_case4_Function() {
        System.out.println("use_case4 Function：默认方法 andThen / compose");
        Function<String, Integer> toLen = String::length;
        Function<Integer, String> toDesc = i -> "长度是" + i;
        // andThen：先 toLen，再 toDesc
        Function<String, String> pipeline = toLen.andThen(toDesc);
        System.out.println("  toLen.andThen(toDesc).apply(\"java\") = " + pipeline.apply("java"));
        // compose：先执行参数函数（与 andThen 方向相反）
        Function<String, String> composed = toDesc.compose(toLen);
        System.out.println("  toDesc.compose(toLen).apply(\"java\") = " + composed.apply("java"));
    }

    // BiFunction<T,U,R>：(T,U) -> R。默认方法 andThen。
    public static void use_case5_BiFunction() {
        System.out.println("use_case5 BiFunction：默认方法 andThen");
        BiFunction<String, Integer, String> concat = (s, i) -> s + i;
        BiFunction<String, Integer, Integer> thenLen = concat.andThen(String::length);
        System.out.println("  concat.andThen(String::length).apply(\"a\", 123) = " + thenLen.apply("a", 123));
    }

    // UnaryOperator<T>：T -> T（Function 的特化）。继承 Function 的默认方法 andThen / compose；自身声明静态方法 identity。
    public static void use_case6_UnaryOperator() {
        System.out.println("use_case6 UnaryOperator：x -> x + 1，默认方法 andThen");
        UnaryOperator<Integer> inc = x -> x + 1;
        UnaryOperator<Integer> doubled = x -> x * 2;
        // 注意：andThen 来自 Function，返回 Function<V,R> 而非 UnaryOperator，故此处类型为 Function
        Function<Integer, Integer> pipeline = inc.andThen(doubled);
        System.out.println("  inc.andThen(doubled).apply(3) = " + pipeline.apply(3));
    }

    // BinaryOperator<T>：(T,T) -> T（BiFunction 的特化）。静态工厂方法 maxBy / minBy。
    public static void use_case7_BinaryOperator() {
        System.out.println("use_case7 BinaryOperator：Integer::sum 与 maxBy/minBy");
        BinaryOperator<Integer> add = Integer::sum;
        System.out.println("  Integer::sum.apply(2,3) = " + add.apply(2, 3));
        BinaryOperator<Integer> max = BinaryOperator.maxBy(Integer::compare);
        BinaryOperator<Integer> min = BinaryOperator.minBy(Integer::compare);
        System.out.println("  maxBy.apply(2,3) = " + max.apply(2, 3));
        System.out.println("  minBy.apply(2,3) = " + min.apply(2, 3));
    }

    // Predicate<T>：T -> boolean。默认方法 and / or / negate；静态方法 isEqual / not。
    public static void use_case8_Predicate() {
        System.out.println("use_case8 Predicate：默认方法 and / or / negate");
        Predicate<Integer> even = x -> x % 2 == 0;
        Predicate<Integer> gt10 = x -> x > 10;
        Predicate<Integer> evenAndGt10 = even.and(gt10);
        System.out.println("  even.and(gt10).test(12) = " + evenAndGt10.test(12));
        System.out.println("  even.and(gt10).test(8) = " + evenAndGt10.test(8));
        System.out.println("  even.negate().test(3) = " + even.negate().test(3));
    }

    // BiPredicate<T,U>：(T,U) -> boolean。默认方法 and / or / negate。
    public static void use_case9_BiPredicate() {
        System.out.println("use_case9 BiPredicate：String::equals 与默认方法 and / or / negate");
        BiPredicate<String, String> equals = String::equals;
        BiPredicate<String, String> sameLen = (a, b) -> a.length() == b.length();
        BiPredicate<String, String> equalsOrSameLen = equals.or(sameLen);
        System.out.println("  String::equals.test(\"a\",\"a\") = " + equals.test("a", "a"));
        System.out.println("  equals.or(sameLen).test(\"ab\",\"cd\") = " + equalsOrSameLen.test("ab", "cd"));
        System.out.println("  sameLen.negate().test(\"ab\",\"cd\") = " + sameLen.negate().test("ab", "cd"));
    }
}
