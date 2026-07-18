package zxf.java.functional.stream;

import zxf.java.functional.stream.account.Account;
import zxf.java.functional.stream.account.AccountFactory;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Collectors 工具类全集：演示各种终结收集器
// 所有用例均复用 account 包的 Account 领域模型
public class StreamCollectCases {

    public static void main(String[] args) {
        use_case1_to_list();
        use_case2_to_set();
        use_case3_to_map();
        use_case4_joining();
        use_case5_grouping_by_single();
        use_case6_grouping_by_with_downstream();
        use_case7_partitioning_by();
        use_case8_counting();
        use_case9_summarizing_int();
        use_case10_mapping();
    }

    private static List<Account> accounts() {
        return AccountFactory.accountStream().toList();
    }

    // toList：收集为 List（不保证实现类型与可变性，当前实现返回可变的 ArrayList；
    // 需要不可变 List 请用 Stream.toList() 或 Collectors.toUnmodifiableList()）
    public static void use_case1_to_list() {
        System.out.println("=== use_case1: Collectors.toList 收集为 List ===");
        List<String> types = accounts().stream()
                .map(Account::getAccountType)
                .collect(Collectors.toList());
        System.out.println("所有类型: " + types);
    }

    // toSet：收集为 Set，自动去重
    public static void use_case2_to_set() {
        System.out.println("\n=== use_case2: Collectors.toSet 去重 ===");
        Set<String> uniqueTypes = accounts().stream()
                .map(Account::getAccountType)
                .collect(Collectors.toSet());
        System.out.println("去重后的类型: " + uniqueTypes);
    }

    // toMap：键值映射；遇到重复键需提供 merge 函数，否则会抛 IllegalStateException
    public static void use_case3_to_map() {
        System.out.println("\n=== use_case3: Collectors.toMap 键值映射（含合并函数）===");
        // accountNumber 可能有重复（SLV/331001 出现两次），用 (a, b) -> a 保留先出现的
        Map<String, String> typeByNumber = accounts().stream()
                .collect(Collectors.toMap(
                        Account::getAccountNumber,
                        Account::getAccountType,
                        (existing, replacement) -> existing));
        System.out.println("编号 -> 类型: " + typeByNumber);
    }

    // joining：把字符串流拼接成单个字符串
    public static void use_case4_joining() {
        System.out.println("\n=== use_case4: Collectors.joining 字符串拼接 ===");
        String joined = accounts().stream()
                .map(Account::getAccountType)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("拼接结果: " + joined);
    }

    // groupingBy(单参)：按分类函数分组，值为元素列表
    public static void use_case5_grouping_by_single() {
        System.out.println("\n=== use_case5: Collectors.groupingBy 单参分组 ===");
        Map<String, List<Account>> byType = accounts().stream()
                .collect(Collectors.groupingBy(Account::getAccountType));
        byType.forEach((type, list) -> System.out.println(type + " -> " + list));
    }

    // groupingBy(双参+下游收集器)：分组后对每组再应用下游收集器（如 counting）
    public static void use_case6_grouping_by_with_downstream() {
        System.out.println("\n=== use_case6: Collectors.groupingBy + 下游 counting 统计各类型数量 ===");
        Map<String, Long> countByType = accounts().stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.counting()));
        countByType.forEach((type, cnt) -> System.out.println(type + " 数量 = " + cnt));
    }

    // partitioningBy：按谓词二分分区，true/false 两组
    public static void use_case7_partitioning_by() {
        System.out.println("\n=== use_case7: Collectors.partitioningBy 二分分区 ===");
        Map<Boolean, List<Account>> partitioned = accounts().stream()
                .collect(Collectors.partitioningBy(Account::isMasterAccount));
        System.out.println("是 MST 账号: " + partitioned.get(true));
        System.out.println("非 MST 账号: " + partitioned.get(false));
    }

    // counting：通常作为下游收集器，统计元素个数
    public static void use_case8_counting() {
        System.out.println("\n=== use_case8: Collectors.counting 统计总数 ===");
        Long total = accounts().stream().collect(Collectors.counting());
        System.out.println("账号总数: " + total);
    }

    // summarizingInt：生成 IntSummaryStatistics，含 count/sum/min/avg/max
    public static void use_case9_summarizing_int() {
        System.out.println("\n=== use_case9: Collectors.summarizingInt 数值汇总 ===");
        IntSummaryStatistics stats = accounts().stream()
                .collect(Collectors.summarizingInt(a -> a.getAccountNumber().length()));
        System.out.println("账号编号长度汇总: " + stats);
    }

    // mapping：对元素先做映射，再交给下游收集器（常用于嵌套收集场景）
    public static void use_case10_mapping() {
        System.out.println("\n=== use_case10: Collectors.mapping 映射 + 下游收集 ===");
        // 按类型分组，但每组只保留编号集合
        Map<String, Set<String>> numbersByType = accounts().stream()
                .collect(Collectors.groupingBy(
                        Account::getAccountType,
                        Collectors.mapping(Account::getAccountNumber, Collectors.toSet())));
        numbersByType.forEach((type, numbers) -> System.out.println(type + " 编号集合 = " + numbers));
    }
}
