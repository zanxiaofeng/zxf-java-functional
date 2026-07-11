package zxf.java.functional.stream;

import zxf.java.functional.stream.account.Account;
import zxf.java.functional.stream.account.AccountFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Stream 中间操作 + 终结操作演示
// 中间操作（无状态/有状态）是惰性的，只有遇到终结操作才会真正执行
// 重点演示 flatMap：把“流中的流”扁平化为一个流
public class StreamIntermediateCases {

    public static void main(String[] args) {
        use_case1_flatmap_nested_list();
        use_case2_flatmap_account_orders();
        use_case3_distinct();
        use_case4_limit();
        use_case5_skip();
        use_case6_takeWhile();
        use_case7_dropWhile();
        use_case8_sorted();
        use_case9_peek();
        use_case10_map_to_int();
        use_case11_count();
        use_case12_min_max();
        use_case13_find_first_find_any();
        use_case14_all_any_none_match();
        use_case15_to_array();
    }

    // flatMap（重点）：把嵌套 List 拍平成单层流
    // map 是“一进一出”，flatMap 是“一进 N 出”，用 flatMap(Function<T, Stream<R>>)
    public static void use_case1_flatmap_nested_list() {
        System.out.println("=== use_case1: flatMap 把嵌套 List 拍平 ===");
        List<List<String>> nested = List.of(
                List.of("Java", "Kotlin"),
                List.of("Scala"),
                List.of("Groovy", "Clojure"));
        // nested 是 List<List<String>>，flatMap 把每个子 List 转成 Stream 再合并
        List<String> flat = nested.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("拍平后: " + flat);
    }

    // flatMap 经典场景：Account → List<Order> 一对多关系
    public static void use_case2_flatmap_account_orders() {
        System.out.println("\n=== use_case2: flatMap 处理 Account → List<Order> 一对多 ===");
        // 为演示，这里内联构造订单数据：一个账号可有多笔订单
        record Order(String accountNumber, String orderId, int amount) {
        }
        record AccountWithOrders(String accountNumber, List<Order> orders) {
        }
        List<AccountWithOrders> data = List.of(
                new AccountWithOrders("551001", List.of(
                        new Order("551001", "O-1", 100),
                        new Order("551001", "O-2", 200))),
                new AccountWithOrders("331001", List.of(
                        new Order("331001", "O-3", 50))),
                new AccountWithOrders("111001", List.of()));
        // 想要：所有账号下的全部订单平铺成一个列表
        List<Order> allOrders = data.stream()
                .flatMap(a -> a.orders().stream())
                .collect(Collectors.toList());
        System.out.println("全部订单数: " + allOrders.size());
        allOrders.forEach(o -> System.out.println("  " + o));

        // 进阶：用 flatMap 求所有订单金额总和
        int totalAmount = data.stream()
                .flatMap(a -> a.orders().stream())
                .mapToInt(Order::amount)
                .sum();
        System.out.println("订单总金额: " + totalAmount);
    }

    // distinct：基于 equals 去重（无状态中间操作）
    public static void use_case3_distinct() {
        System.out.println("\n=== use_case3: distinct 去重 ===");
        List<Integer> distinct = Stream.of(1, 2, 2, 3, 3, 3, 4)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("去重后: " + distinct);
        // 应用到 Account：accountStream 中有重复的 SLV/331001
        List<String> numbers = AccountFactory.accountStream()
                .map(Account::getAccountNumber)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("账号编号去重: " + numbers);
    }

    // limit：截断前 N 个（短路中间操作）
    public static void use_case4_limit() {
        System.out.println("\n=== use_case4: limit 截取前 N 个 ===");
        List<Integer> limited = Stream.of(1, 2, 3, 4, 5)
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("limit(3): " + limited);
    }

    // skip：跳过前 N 个
    public static void use_case5_skip() {
        System.out.println("\n=== use_case5: skip 跳过前 N 个 ===");
        List<Integer> skipped = Stream.of(1, 2, 3, 4, 5)
                .skip(3)
                .collect(Collectors.toList());
        System.out.println("skip(3): " + skipped);
    }

    // takeWhile：JDK 9 新增，取满足条件的前缀（遇到第一个不满足即停）
    public static void use_case6_takeWhile() {
        System.out.println("\n=== use_case6: takeWhile 取前缀（升序中取 < 4 的部分）===");
        List<Integer> taken = Stream.of(1, 2, 3, 4, 5, 2, 1)
                .takeWhile(n -> n < 4)
                .collect(Collectors.toList());
        System.out.println("takeWhile(n < 4): " + taken + "（遇到 4 即停，后面不再看）");
    }

    // dropWhile：JDK 9 新增，丢弃满足条件的前缀，返回剩余元素
    public static void use_case7_dropWhile() {
        System.out.println("\n=== use_case7: dropWhile 丢弃前缀 ===");
        List<Integer> dropped = Stream.of(1, 2, 3, 4, 5, 2, 1)
                .dropWhile(n -> n < 4)
                .collect(Collectors.toList());
        System.out.println("dropWhile(n < 4): " + dropped);
    }

    // sorted：有状态中间操作，按自然序或指定 Comparator 排序
    public static void use_case8_sorted() {
        System.out.println("\n=== use_case8: sorted 排序 ===");
        List<Integer> natural = Stream.of(5, 3, 1, 4, 2)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("自然序: " + natural);
        List<Integer> reversed = Stream.of(5, 3, 1, 4, 2)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        System.out.println("逆序: " + reversed);
    }

    // peek：调试用的中间操作，对流中每个元素执行一个动作但不改变元素
    public static void use_case9_peek() {
        System.out.println("\n=== use_case9: peek 中间调试 ===");
        // peek 是惰性的：没有终结操作时不会执行
        List<Integer> result = Stream.of(1, 2, 3, 4, 5)
                .peek(n -> System.out.println("  处理前: " + n))
                .filter(n -> n % 2 == 0)
                .peek(n -> System.out.println("  过滤后: " + n))
                .collect(Collectors.toList());
        System.out.println("最终结果（偶数）: " + result);
    }

    // mapToInt：把对象流映射为 IntStream，获得原生数值聚合能力
    public static void use_case10_map_to_int() {
        System.out.println("\n=== use_case10: mapToInt 转基本类型流 ===");
        List<Account> accounts = AccountFactory.accountStream().toList();
        int totalLength = accounts.stream()
                .mapToInt(a -> a.getAccountNumber().length())
                .sum();
        System.out.println("所有账号编号长度之和: " + totalLength);
    }

    // count：终结操作，统计元素个数
    public static void use_case11_count() {
        System.out.println("\n=== use_case11: count 统计个数 ===");
        long count = AccountFactory.accountStream()
                .filter(a -> "MST".equals(a.getAccountType()))
                .count();
        System.out.println("MST 账号数量: " + count);
    }

    // min / max：终结操作，返回 Optional（流为空时返回 empty）
    public static void use_case12_min_max() {
        System.out.println("\n=== use_case12: min / max 求极值（返回 Optional）===");
        OptionalInt min = IntStream.of(7, 2, 9, 4).min();
        OptionalInt max = IntStream.of(7, 2, 9, 4).max();
        System.out.println("min = " + min + ", max = " + max);
        // 对象流需提供 Comparator
        Optional<Account> longestNumber = AccountFactory.accountStream()
                .max(Comparator.comparingInt(a -> a.getAccountNumber().length()));
        System.out.println("编号最长的账号: " + longestNumber);
    }

    // findFirst / findAny：短路终结操作
    // findFirst 返回相遇顺序中的第一个；findAny 在并行流中可能返回任意一个以提升性能
    public static void use_case13_find_first_find_any() {
        System.out.println("\n=== use_case13: findFirst / findAny ===");
        Optional<Integer> first = Stream.of(10, 20, 30, 40).filter(n -> n > 15).findFirst();
        Optional<Integer> any = Stream.of(10, 20, 30, 40).parallel().filter(n -> n > 15).findAny();
        System.out.println("findFirst(>15) = " + first);
        System.out.println("findAny(>15, 并行) = " + any);
    }

    // allMatch / anyMatch / noneMatch：短路终结操作，返回 boolean
    public static void use_case14_all_any_none_match() {
        System.out.println("\n=== use_case14: allMatch / anyMatch / noneMatch ===");
        boolean allPositive = Stream.of(1, 2, 3).allMatch(n -> n > 0);
        boolean anyBig = Stream.of(1, 2, 3).anyMatch(n -> n > 10);
        boolean noneZero = Stream.of(1, 2, 3).noneMatch(n -> n == 0);
        System.out.println("全部 > 0: " + allPositive);
        System.out.println("存在 > 10: " + anyBig);
        System.out.println("都不为 0: " + noneZero);
    }

    // toArray：终结操作，把流元素收集成数组
    public static void use_case15_to_array() {
        System.out.println("\n=== use_case15: toArray 收集为数组 ===");
        Object[] objects = Stream.of("A", "B", "C").toArray();
        // 指定生成器数组类型，可得到强类型数组
        String[] typed = Stream.of("A", "B", "C").toArray(String[]::new);
        System.out.println("Object[]: " + Arrays.toString(objects));
        System.out.println("String[]: " + Arrays.toString(typed));
    }
}
