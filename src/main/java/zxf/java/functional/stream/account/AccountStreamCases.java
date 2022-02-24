package zxf.java.functional.stream.account;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        List<Account> masterAccounts = AccountFactory.accountStream()
                .filter(account -> "MST".equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneMasterAccounts = AccountFactory.accountStream()
                .filter(account -> !"MST".equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> masterOrSlaveAccounts = AccountFactory.accountStream()
                .filter(account -> "MST".equals(account.getAccountType())
                        || "SLV".equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_basic_method_use_case() {
        System.out.println("MST");
        List<Account> masterAccounts = AccountFactory.accountStream()
                .filter(account -> account.isAccountOf("MST"))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneMasterAccounts = AccountFactory.accountStream()
                .filter(account -> !account.isAccountOf("MST"))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> masterOrSlaveAccounts = AccountFactory.accountStream()
                .filter(account -> account.isAccountOfAny("MST", "SLV"))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_convenience_method_use_case() {
        System.out.println("MST");
        List<Account> masterAccounts = AccountFactory.accountStream()
                .filter(Account::isMasterAccount)
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneMasterAccounts = AccountFactory.accountStream()
                .filter(Predicate.not(Account::isMasterAccount))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> masterOrSlaveAccounts = AccountFactory.accountStream()
                .filter(Account::isMasterOrSlaveAccount)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_basic_predicate_use_case() {
        System.out.println("MST");
        List<Account> masterAccounts = AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType("MST"))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneMasterAccounts = AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType("MST").negate())
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> masterOrSlaveAccounts = AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfAnyTypes("MST", "SLV"))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_constant_predicate_use_case() {
        System.out.println("MST");
        List<Account> masterAccounts = AccountFactory.accountStream()
                .filter(Account.Predicates.IS_MASTER)
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneMasterAccounts = AccountFactory.accountStream()
                .filter(Account.Predicates.IS_MASTER.negate())
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> masterOrSlaveAccounts = AccountFactory.accountStream()
                .filter(Account.Predicates.IS_MASTER_OR_SLAVE)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void dynamic_use_case(String... accountTypes) {
        System.out.println("MST");
        List<Account> type0Accounts = AccountFactory.accountStream()
                .filter(account -> accountTypes[0].equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneType0Accounts = AccountFactory.accountStream()
                .filter(account -> !accountTypes[0].equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> selectedAccounts = AccountFactory.accountStream()
                .filter(account -> List.of(accountTypes).contains(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void dynamic_basic_method_use_case(String... accountTypes) {
        System.out.println("MST");
        List<Account> type0Accounts = AccountFactory.accountStream()
                .filter(account -> account.isAccountOf(accountTypes[0]))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneType0Accounts = AccountFactory.accountStream()
                .filter(account -> !account.isAccountOf(accountTypes[0]))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> selectedAccounts = AccountFactory.accountStream()
                .filter(account -> !account.isAccountOfAny(accountTypes))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void dynamic_basic_predicate_use_case(String... accountTypes) {
        System.out.println("MST");
        List<Account> type0Accounts = AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("none-MST");
        List<Account> noneType0Accounts = AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]).negate())
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("MST or SLV");
        List<Account> selectedAccounts = AccountFactory.accountStream()
                .filter(Account.Predicates.isAccountOfAnyTypes(accountTypes))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }
}
