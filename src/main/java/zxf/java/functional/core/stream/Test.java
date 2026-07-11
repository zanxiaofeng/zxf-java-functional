package zxf.java.functional.core.stream;

import java.util.Arrays;
import java.util.List;

/**
 * 自实现 Stream 流水线的冒烟测试 / 教学 Demo。
 *
 * <p>对应 Readme 中「惰性流 + Processor 责任链」的概念：
 * {@link Stream} 通过 {@link Processor} 链式包装（{@link InitialProcessor}
 * → {@link FilterProcessor} → {@link MapProcessor}）实现惰性求值，
 * 终端操作（collect / forEach / reduce 等）才真正拉取数据。</p>
 */
public class Test {
    public static void main(String[] args) {
        use_case1_filter_map_collect();
        use_case2_filter_map_joining();
        use_case3_null_element_no_infinite_loop();
        use_case4_reduce_count_foreach();
        use_case5_flatMap();
    }

    // filter + map + collect(toList)：保留偶数并转字符串
    public static void use_case1_filter_map_collect() {
        System.out.println("use_case1 filter+map+collect(toList)");
        Stream<Integer> ints = Stream.list(Arrays.asList(1, 2, 3, 4));
        List<String> result = ints.filter(i -> i % 2 == 0).map(String::valueOf).collect(Collector.toList());
        System.out.println(result.size());
    }

    // filter + map + collect(joining)：偶数拼接成字符串
    public static void use_case2_filter_map_joining() {
        System.out.println("use_case2 filter+map+collect(joining)");
        Stream<Integer> numbers = Stream.list(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        String numberResult = numbers.filter(i -> i % 2 == 0).map(String::valueOf).collect(Collector.joining());
        System.out.println(numberResult);
    }

    // 回归验证 P0-1：源头含 null 不再死循环
    // 旧实现中 MapProcessor/FilterProcessor 用 null 作「未取到值」哨兵，
    // 遇到合法 null 元素会 while(prevNext==null) 无限循环。
    public static void use_case3_null_element_no_infinite_loop() {
        System.out.println("use_case3 源头含 null 元素（验证不再死循环）");
        // 1 在前，null 在后；map 把偶数转字符串，奇数转 null
        Stream<Integer> withNull = Stream.list(Arrays.asList(1, 2, null, 3, 4));
        // filter 跳过 null（Predicate 显式判空），map 对非空偶数转字符串
        List<String> result = withNull
                .filter(java.util.Objects::nonNull)
                .filter(i -> i % 2 == 0)
                .map(String::valueOf)
                .collect(Collector.toList());
        System.out.println("过滤后的偶数 = " + result);

        // 进一步验证 mapper 返回 null 也不会死循环
        Stream<Integer> src = Stream.list(Arrays.asList(1, 2, 3));
        List<String> mapped = src.map(i -> (i % 2 == 0) ? String.valueOf(i) : null).collect(Collector.toList());
        System.out.println("map 返回含 null = " + mapped);
    }

    // reduce / count / forEach 终端操作演示（对应 Readme 的 reduce 终端操作）
    public static void use_case4_reduce_count_foreach() {
        System.out.println("use_case4 reduce/count/forEach");
        // reduce(identity, reducer)：求和
        Integer sum = Stream.list(Arrays.asList(1, 2, 3, 4, 5)).reduce(0, Integer::sum);
        System.out.println("1..5 求和 = " + sum);

        // reduce(BinaryOperator)：无 identity，返回 java.util.Optional
        java.util.Optional<Integer> max = Stream.list(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6)).reduce(Integer::max);
        System.out.println("最大值 = " + max);

        // count：元素个数
        long count = Stream.list(Arrays.asList(1, 2, 3, 4, 5)).filter(i -> i % 2 == 0).count();
        System.out.println("偶数个数 = " + count);

        // forEach：内部迭代消费
        System.out.print("forEach 输出 = ");
        Stream.list(Arrays.asList(1, 2, 3)).forEach(i -> System.out.print(i + " "));
        System.out.println();
    }

    // flatMap：每个元素展开成多个元素（对应 Readme 的 Monad flatten 概念）
    public static void use_case5_flatMap() {
        System.out.println("use_case5 flatMap");
        // 1 -> [1,10], 2 -> [2,20], 3 -> [3,30]
        List<Integer> expanded = Stream.list(Arrays.asList(1, 2, 3))
                .flatMap(i -> Stream.list(Arrays.asList(i, i * 10)))
                .collect(Collector.toList());
        System.out.println("flatMap 展开 = " + expanded);
    }
}
