package zxf.java.functional.pattern.closure.account;

import java.util.Arrays;
import java.util.List;

/**
 * 闭包作为谓词工厂演示，对应 Readme「闭包」章节。
 * Account.Predicates.isAccountOfType / isAccountOfAnyTypes 都是返回 Predicate<Account> 的工厂方法，
 * 它们捕获入参（accountType / accountTypes）形成闭包，是"闭包 = 捕获上下文的函数对象"的直接体现。
 *
 * 注意：Stream 只能消费一次，同一 Stream 连续做多次终端操作会抛 IllegalStateException。
 * 因此本类方法接收 List<Account>，每次过滤都新建 Stream，避免复用已消费的 Stream。
 */
public class AccountCases {

    public static void main(String[] args) {
        List<Account> accounts = Arrays.asList(
                new Account("MST", "001"),
                new Account("SLV", "002"),
                new Account("CHK", "003"),
                new Account("MST", "004"));

        System.out.println("#case 1: 静态谓词工厂（basic_predicate_static_use_case）");
        basic_predicate_static_use_case(accounts);

        System.out.println("\n#case 2: 常量谓词（constant_predicate_static_use_case）");
        constant_predicate_static_use_case(accounts);

        System.out.println("\n#case 3: 动态谓词工厂（basic_predicate_dynamic_use_case）");
        basic_predicate_dynamic_use_case(accounts, "MST", "SLV", "CHK");
    }

    // 静态使用：调用 isAccountOfType / isAccountOfAnyTypes 工厂方法得到闭包谓词
    public static void basic_predicate_static_use_case(List<Account> accounts) {
        List<Account> masterAccounts = accounts.stream()
                .filter(Account.Predicates.isAccountOfType("MST"))
                .toList();
        System.out.println("  isAccountOfType(\"MST\") => " + masterAccounts);

        List<Account> noneMasterAccounts = accounts.stream()
                .filter(Account.Predicates.isAccountOfType("MST").negate())
                .toList();
        System.out.println("  isAccountOfType(\"MST\").negate() => " + noneMasterAccounts);

        List<Account> masterOrSlaveAccounts = accounts.stream()
                .filter(Account.Predicates.isAccountOfAnyTypes("MST", "SLV"))
                .toList();
        System.out.println("  isAccountOfAnyTypes(\"MST\",\"SLV\") => " + masterOrSlaveAccounts);
    }

    // 常量谓词：直接复用预定义的 IS_MASTER / IS_MASTER_OR_SLAVE 常量闭包
    public static void constant_predicate_static_use_case(List<Account> accounts) {
        List<Account> masterAccounts = accounts.stream()
                .filter(Account.Predicates.IS_MASTER)
                .toList();
        System.out.println("  IS_MASTER => " + masterAccounts);

        List<Account> noneMasterAccounts = accounts.stream()
                .filter(Account.Predicates.IS_MASTER.negate())
                .toList();
        System.out.println("  IS_MASTER.negate() => " + noneMasterAccounts);

        List<Account> masterOrSlaveAccounts = accounts.stream()
                .filter(Account.Predicates.IS_MASTER_OR_SLAVE)
                .toList();
        System.out.println("  IS_MASTER_OR_SLAVE => " + masterOrSlaveAccounts);
    }

    // 动态使用：运行时传入账户类型数组，工厂方法据此生成闭包谓词
    public static void basic_predicate_dynamic_use_case(List<Account> accounts, String... accountTypes) {
        List<Account> type0Accounts = accounts.stream()
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]))
                .toList();
        System.out.println("  isAccountOfType(accountTypes[0]) => " + type0Accounts);

        List<Account> noneType0Accounts = accounts.stream()
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]).negate())
                .toList();
        System.out.println("  isAccountOfType(accountTypes[0]).negate() => " + noneType0Accounts);

        List<Account> selectedAccounts = accounts.stream()
                .filter(Account.Predicates.isAccountOfAnyTypes(accountTypes))
                .toList();
        System.out.println("  isAccountOfAnyTypes(accountTypes) => " + selectedAccounts);
    }
}
