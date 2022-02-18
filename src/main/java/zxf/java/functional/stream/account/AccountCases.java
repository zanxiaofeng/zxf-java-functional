package zxf.java.functional.stream.account;

import java.util.stream.Stream;

public class AccountCases {
    public static void main(String[] args) {
        use_case1_1();
        use_case1_２();
        use_case2_1();
        use_case2_2();
    }

    //快捷方法测试
    public static void use_case1_1() {
        System.out.println("use_case1_1 快捷方法测试");

        System.out.println("MST:");
        accountStream().filter(Account::isMasterAccount).forEach(System.out::println);

        System.out.println("SLV:");
        accountStream().filter(Account::isSlaveAccount).forEach(System.out::println);

        System.out.println("OTH:");
        accountStream().filter(Account::isOtherAccount).forEach(System.out::println);

        System.out.println("MST&SLV:");
        accountStream().filter(Account::isMasterOrSlaveAccount).forEach(System.out::println);

        System.out.println("MST&OTh:");
        accountStream().filter(Account::isMasterOrOtherAccount).forEach(System.out::println);

        System.out.println("SLV&OTh:");
        accountStream().filter(Account::isSlaveOrOtherAccount).forEach(System.out::println);
    }

    //Lambda快捷常量测试
    public static void use_case1_２() {
        System.out.println("use_case1_２ Lambda快捷常量测试");

        System.out.println("MST:");
        accountStream().filter(Account.Predicates.IS_MASTER).forEach(System.out::println);

        System.out.println("SLV:");
        accountStream().filter(Account.Predicates.IS_SLAVE).forEach(System.out::println);

        System.out.println("OTH:");
        accountStream().filter(Account.Predicates.IS_OTHER).forEach(System.out::println);

        System.out.println("MST&SLV:");
        accountStream().filter(Account.Predicates.IS_MASTER_OR_SLAVE).forEach(System.out::println);

        System.out.println("MST&OTh:");
        accountStream().filter(Account.Predicates.IS_MASTER_OR_OTHER).forEach(System.out::println);

        System.out.println("SLV&OTh:");
        accountStream().filter(Account.Predicates.IS_SLAVE_OR_OTHER).forEach(System.out::println);
    }

    //基础方法测试
    public static void use_case2_1() {
        System.out.println("use_case2_1 基础方法测试");

        System.out.println("OTH:");
        accountStream().filter(account -> account.isAccountOf("OTH")).forEach(System.out::println);

        System.out.println("MST&OTH:");
        accountStream().filter(account -> account.isAccountOfAny("MST", "OTH")).forEach(System.out::println);
    }

    //Lambda支持方法测试
    public static void use_case2_2() {
        System.out.println("use_case2_2 Lambda支持方法测试");

        System.out.println("OTH:");
        accountStream().filter(Account.Predicates.isAccountOfType("OTH")).forEach(System.out::println);

        System.out.println("MST&OTH:");
        accountStream().filter(Account.Predicates.isAccountOfAnyTypes("MST", "OTH")).forEach(System.out::println);
    }

    private static Stream<Account> accountStream() {
        return Stream.of(new Account("MST", "123"),
                new Account("SLV", "321"),
                new Account("OTH", "111"));
    }
}
