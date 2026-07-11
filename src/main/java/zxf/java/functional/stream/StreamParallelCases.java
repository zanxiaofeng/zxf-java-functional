package zxf.java.functional.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 并行流（Parallel Stream）演示：演示并行执行、有序性、以及共享可变状态陷阱
// 核心要点：并行流底层使用 ForkJoinPool.commonPool()，适合 CPU 密集型且无共享可变状态的处理
public class StreamParallelCases {

    public static void main(String[] args) {
        use_case1_parallel_from_collection();
        use_case2_parallel_from_stream();
        use_case3_sequential_switch_back();
        use_case4_for_each_ordered();
        use_case5_ordering_in_parallel();
        use_case6_shared_mutable_state_pitfall();
        use_case7_correct_with_collect();
        use_case8_correct_with_synchronized_list();
    }

    // Collection.parallelStream()：从集合直接创建并行流
    public static void use_case1_parallel_from_collection() {
        System.out.println("=== use_case1: Collection.parallelStream() 创建并行流 ===");
        List<Integer> numbers = IntStream.rangeClosed(1, 5).boxed().toList();
        // isParallel() 可判断当前流是否为并行流
        boolean isParallel = numbers.parallelStream().isParallel();
        System.out.println("parallelStream 是否为并行: " + isParallel);
        numbers.parallelStream().forEach(n -> System.out.println("  处理: " + n + " @ " + Thread.currentThread().getName()));
    }

    // Stream.parallel()：把一个串行流转换为并行流
    public static void use_case2_parallel_from_stream() {
        System.out.println("\n=== use_case2: Stream.parallel() 转并行流 ===");
        boolean isParallel = IntStream.range(1, 6).boxed()
                .parallel()
                .isParallel();
        System.out.println("调用 parallel() 后是否为并行: " + isParallel);
    }

    // Stream.sequential()：把并行流再切回串行流
    public static void use_case3_sequential_switch_back() {
        System.out.println("\n=== use_case3: Stream.sequential() 切回串行 ===");
        boolean parallel = IntStream.range(1, 6).boxed().parallel().isParallel();
        boolean sequential = IntStream.range(1, 6).boxed().parallel().sequential().isParallel();
        System.out.println("parallel 后 isParallel = " + parallel);
        System.out.println("再 sequential 后 isParallel = " + sequential);
    }

    // forEachOrdered：并行流中仍按相遇顺序（encounter order）逐个处理
    public static void use_case4_for_each_ordered() {
        System.out.println("\n=== use_case4: forEachOrdered 并行流保持顺序 ===");
        System.out.println("普通 forEach（顺序可能乱）:");
        IntStream.rangeClosed(1, 5).parallel().forEach(n -> System.out.println("  " + n));
        System.out.println("forEachOrdered（保持 1..5 顺序）:");
        IntStream.rangeClosed(1, 5).parallel().forEachOrdered(n -> System.out.println("  " + n));
    }

    // 并行流的相遇顺序：有顺序的源（List）即便并行，终结操作结果仍保留顺序
    public static void use_case5_ordering_in_parallel() {
        System.out.println("\n=== use_case5: 并行流的相遇顺序（ordered source）===");
        List<Integer> source = IntStream.rangeClosed(1, 10).boxed().toList();
        // 对有序源，collect 出的 List 仍是 1..10 顺序，即使并行处理
        List<Integer> collected = source.parallelStream()
                .map(x -> x * 2)
                .collect(Collectors.toList());
        System.out.println("并行 map 后 collect 结果: " + collected);
        // unordered() 可显式去掉相遇顺序约束，在部分场景下能提升并行性能
        boolean isParallelAfterUnordered = source.parallelStream().unordered().isParallel();
        System.out.println("unordered() 不改变并行性，仍为并行: " + isParallelAfterUnordered);
    }

    // 陷阱演示：并行流 + forEach + 共享非线程安全集合 = 数据丢失/并发异常
    public static void use_case6_shared_mutable_state_pitfall() {
        System.out.println("\n=== use_case6: 陷阱 — 并行 forEach 改共享 ArrayList（错误做法）===");
        // ArrayList 非线程安全，多线程并发 add 会丢失元素或抛 ArrayIndexOutOfBoundsException
        List<Integer> unsafe = new ArrayList<>();
        List<Integer> source = IntStream.rangeClosed(1, 1000).boxed().toList();
        source.parallelStream().forEach(unsafe::add);
        System.out.println("源元素个数: 1000, 实际 add 进 ArrayList 个数: " + unsafe.size() + "（通常少于 1000）");
        System.out.println("结论: forEach + 共享可变状态在并行流中是反模式，结果不可预测");
    }

    // 正确做法一：使用 collect(Collectors.toList())，收集器内部会安全合并部分结果
    public static void use_case7_correct_with_collect() {
        System.out.println("\n=== use_case7: 正确做法一 — collect 收集器（线程安全合并）===");
        List<Integer> source = IntStream.rangeClosed(1, 1000).boxed().toList();
        List<Integer> result = source.parallelStream()
                .collect(Collectors.toList());
        System.out.println("collect 后元素个数: " + result.size() + "（应等于 1000）");
    }

    // 正确做法二：使用 Collections.synchronizedList 包装出线程安全 List
    public static void use_case8_correct_with_synchronized_list() {
        System.out.println("\n=== use_case8: 正确做法二 — synchronizedList 线程安全包装 ===");
        List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
        List<Integer> source = IntStream.rangeClosed(1, 1000).boxed().toList();
        source.parallelStream().forEach(syncList::add);
        System.out.println("synchronizedList add 后元素个数: " + syncList.size() + "（应等于 1000）");
        System.out.println("提示: 优先使用 collect（无锁、更高效），synchronizedList 是兜底手段");
    }
}
