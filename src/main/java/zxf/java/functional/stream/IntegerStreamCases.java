package zxf.java.functional.stream;

import zxf.java.functional.stream.account.Account;
import zxf.java.functional.stream.account.AccountFactory;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

// 基本类型流（Primitive Streams）演示：IntStream / LongStream / DoubleStream
// 优势：避免装箱拆箱开销，并提供 sum/average/summaryStatistics 等原生聚合方法
public class IntegerStreamCases {

    public static void main(String[] args) {
        use_case1_intstream_range();
        use_case2_intstream_rangeclosed();
        use_case3_intstream_of();
        use_case4_intstream_iterate();
        use_case5_summary_statistics();
        use_case6_average_sum_max_min();
        use_case7_longstream_range();
        use_case8_doublestream_generate();
        use_case9_map_to_int();
    }

    // IntStream.range：[start, end) 半开区间，不包含 end
    public static void use_case1_intstream_range() {
        System.out.println("=== use_case1: IntStream.range 半开区间 [1,5) ===");
        IntStream.range(1, 5).forEach(n -> System.out.println("  " + n));
    }

    // IntStream.rangeClosed：[start, end] 闭区间，包含 end
    public static void use_case2_intstream_rangeclosed() {
        System.out.println("\n=== use_case2: IntStream.rangeClosed 闭区间 [1,5] ===");
        IntStream.rangeClosed(1, 5).forEach(n -> System.out.println("  " + n));
    }

    // IntStream.of：从指定值列表创建
    public static void use_case3_intstream_of() {
        System.out.println("\n=== use_case3: IntStream.of 指定元素 ===");
        IntStream.of(10, 30, 20, 50, 40)
                .forEach(n -> System.out.println("  " + n));
    }

    // IntStream.iterate：基于种子和函数迭代，需 limit 截断无限流
    public static void use_case4_intstream_iterate() {
        System.out.println("\n=== use_case4: IntStream.iterate 迭代（乘 2）===");
        IntStream.iterate(1, n -> n * 2)
                .limit(6)
                .forEach(n -> System.out.println("  " + n));
    }

    // summaryStatistics()：一次调用获取 count/sum/min/avg/max 五项统计
    public static void use_case5_summary_statistics() {
        System.out.println("\n=== use_case5: summaryStatistics 一次性统计 ===");
        IntSummaryStatistics stats = IntStream.of(10, 30, 20, 50, 40).summaryStatistics();
        System.out.println("count = " + stats.getCount());
        System.out.println("sum   = " + stats.getSum());
        System.out.println("min   = " + stats.getMin());
        System.out.println("max   = " + stats.getMax());
        System.out.println("avg   = " + stats.getAverage());
    }

    // average / sum / max / min：基本类型流的内置聚合（返回 OptionalDouble 或 OptionalInt）
    public static void use_case6_average_sum_max_min() {
        System.out.println("\n=== use_case6: average/sum/max/min 内置聚合 ===");
        int sum = IntStream.of(10, 30, 20, 50, 40).sum();
        double avg = IntStream.of(10, 30, 20, 50, 40).average().orElse(0);
        int max = IntStream.of(10, 30, 20, 50, 40).max().orElse(0);
        int min = IntStream.of(10, 30, 20, 50, 40).min().orElse(0);
        System.out.println("sum = " + sum + ", avg = " + avg + ", max = " + max + ", min = " + min);
    }

    // LongStream.range：用于更大范围的 long 区间生成
    public static void use_case7_longstream_range() {
        System.out.println("\n=== use_case7: LongStream.range ===");
        long count = LongStream.range(1L, 1_000_000L).count();
        System.out.println("区间 [1, 1000000) 元素个数: " + count);
    }

    // DoubleStream.generate：基于 Supplier 生成 double 流，需 limit 截断
    public static void use_case8_doublestream_generate() {
        System.out.println("\n=== use_case8: DoubleStream.generate（随机数）===");
        DoubleStream.generate(Math::random)
                .limit(3)
                .forEach(d -> System.out.println("  随机值: " + d));
    }

    // mapToInt：从对象流转换为 IntStream，避免装箱开销，便于数值聚合
    public static void use_case9_map_to_int() {
        System.out.println("\n=== use_case9: mapToInt 从对象流转换（账号编号长度）===");
        List<Account> accounts = AccountFactory.accountStream().toList();
        IntSummaryStatistics stats = accounts.stream()
                .mapToInt(a -> a.getAccountNumber().length())
                .summaryStatistics();
        System.out.println("账号编号长度统计: max=" + stats.getMax() + ", min=" + stats.getMin() + ", avg=" + stats.getAverage());
    }
}
