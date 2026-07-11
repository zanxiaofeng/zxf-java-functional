package zxf.java.functional.stream;

import java.util.Optional;
import java.util.stream.Stream;

// Stream.reduce 归约演示：三种重载形式
// reduce 把流中所有元素反复结合，最终得到一个值
public class StreamReduceCases {

    public static void main(String[] args) {
        use_case1_reduce_optional();
        use_case2_reduce_with_identity();
        use_case3_reduce_three_args();
    }

    // 形式一：Optional<T> reduce(BinaryOperator<T>)
    // 无初始值，流为空时返回 Optional.empty()，因此返回类型是 Optional
    public static void use_case1_reduce_optional() {
        System.out.println("=== use_case1: reduce(BinaryOperator) 求和（返回 Optional）===");
        Optional<Integer> sum = Stream.of(1, 2, 3, 4, 5).reduce(Integer::sum);
        System.out.println("1+2+3+4+5 = " + sum.orElse(0));

        // 空流场景
        Optional<Integer> empty = Stream.<Integer>empty().reduce(Integer::sum);
        System.out.println("空流 reduce 结果存在: " + empty.isPresent());
    }

    // 形式二：T reduce(identity, BinaryOperator<T>)
    // 有初始值 identity，流为空时直接返回 identity，因此返回类型确定是 T
    public static void use_case2_reduce_with_identity() {
        System.out.println("\n=== use_case2: reduce(identity, BinaryOperator) 求最大值 ===");
        int max = Stream.of(3, 7, 2, 9, 4).reduce(Integer.MIN_VALUE, Integer::max);
        System.out.println("最大值 = " + max);

        // 求乘积：identity 为 1（乘法单位元）
        int product = Stream.of(1, 2, 3, 4).reduce(1, (a, b) -> a * b);
        System.out.println("1*2*3*4 = " + product);
    }

    // 形式三：<U> U reduce(identity, BiFunction<U,T,U>, BinaryOperator<U>)
    // 支持返回类型 U 与流元素类型 T 不同；第三参 combiner 用于并行合并部分结果
    public static void use_case3_reduce_three_args() {
        System.out.println("\n=== use_case3: reduce(identity, BiFunction, BinaryOperator) 复合归约 ===");
        // 场景：把 Integer 流归约成 StringBuilder，并统计字符总长度
        // accumulator：把每个数字追加到 StringBuilder
        // combiner：并行场景下合并两个 StringBuilder
        StringBuilder result = Stream.of(1, 22, 333)
                .reduce(
                        new StringBuilder(),
                        (sb, n) -> sb.append(n).append(","),
                        StringBuilder::append);
        System.out.println("拼接结果: " + result);

        // 复合场景：同时计算总和与个数（自定义 Pair）
        int combined = Stream.of(1, 2, 3, 4, 5)
                .reduce(
                        0,
                        (acc, n) -> acc + n,           // 累加元素
                        Integer::sum);                  // 合并部分结果
        System.out.println("三参 reduce 求和 = " + combined);
    }
}
