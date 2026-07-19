package zxf.java.functional.stream.account;

import java.util.List;
import java.util.function.Predicate;

public class AccountStreamCases {
    public static void main(String[] args) {
        System.out.println("static_use_case");
        static_use_case();

        System.out.println("static_basic_method_use_case");
        static_basic_method_use_case();

        System.out.println("static_convenience_method_use_case");
        static_convenience_method_use_case();

        System.out.println("static_basic_predicate_use_case");
        static_basic_predicate_use_case();

        System.out.println("static_constant_predicate_use_case");
        static_constant_predicate_use_case();

        System.out.println("dynamic_use_case");
        dynamic_use_case("MST", "SLV");

        System.out.println("dynamic_basic_method_use_case");
        dynamic_basic_method_use_case("MST", "SLV");

        System.out.println("dynamic_basic_predicate_use_case");
        dynamic_basic_predicate_use_case("MST", "SLV");
    }

    private static void static_use_case() {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(account -> "MST".equals(account.getAccountType()))
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(account -> !"MST".equals(account.getAccountType()))
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(account -> "MST".equals(account.getAccountType())
                        || "SLV".equals(account.getAccountType()))
                .forEach(System.out::println);
    }

    private static void static_basic_method_use_case() {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(account -> account.isAccountOf("MST"))
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(account -> !account.isAccountOf("MST"))
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(account -> account.isAccountOfAny("MST", "SLV"))
                .forEach(System.out::println);
    }

    private static void static_convenience_method_use_case() {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(Account::isMasterAccount)
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(Predicate.not(Account::isMasterAccount))
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(Account::isMasterOrSlaveAccount)
                .forEach(System.out::println);
    }

    private static void static_basic_predicate_use_case() {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType("MST"))
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType("MST").negate())
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfAnyTypes("MST", "SLV"))
                .forEach(System.out::println);
    }

    private static void static_constant_predicate_use_case() {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(Account.Predicates.IS_MASTER)
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(Account.Predicates.IS_MASTER.negate())
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(Account.Predicates.IS_MASTER_OR_SLAVE)
                .forEach(System.out::println);
    }

    private static void dynamic_use_case(String... accountTypes) {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(account -> accountTypes[0].equals(account.getAccountType()))
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(account -> !accountTypes[0].equals(account.getAccountType()))
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(account -> List.of(accountTypes).contains(account.getAccountType()))
                .forEach(System.out::println);
    }

    private static void dynamic_basic_method_use_case(String... accountTypes) {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(account -> account.isAccountOf(accountTypes[0]))
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(account -> !account.isAccountOf(accountTypes[0]))
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(account -> account.isAccountOfAny(accountTypes))
                .forEach(System.out::println);
    }

    private static void dynamic_basic_predicate_use_case(String... accountTypes) {
        System.out.println("MST");
        AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]))
                .forEach(System.out::println);
        System.out.println("none-MST");
        AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]).negate())
                .forEach(System.out::println);
        System.out.println("MST or SLV");
        AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfAnyTypes(accountTypes))
                .forEach(System.out::println);
    }
}
