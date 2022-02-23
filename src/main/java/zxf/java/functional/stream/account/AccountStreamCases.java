package zxf.java.functional.stream.account;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountStreamCases {

    private static void static_use_case(Stream<Account> accountStream) {
        List<Account> masterAccounts = accountStream
                .filter(account -> "MST".equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneMasterAccounts = accountStream
                .filter(account -> !"MST".equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> masterOrSlaveAccounts = accountStream
                .filter(account -> "MST".equals(account.getAccountType())
                        || "SLV".equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_basic_method_use_case(Stream<Account> accountStream) {
        List<Account> masterAccounts = accountStream
                .filter(account -> account.isAccountOf("MST"))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneMasterAccounts = accountStream
                .filter(account -> !account.isAccountOf("MST"))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> masterOrSlaveAccounts = accountStream
                .filter(account -> account.isAccountOfAny("MST", "SLV"))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_convenience_method_use_case(Stream<Account> accountStream) {
        List<Account> masterAccounts = accountStream
                .filter(Account::isMasterAccount)
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneMasterAccounts = accountStream
                .filter(Predicate.not(Account::isMasterAccount))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> masterOrSlaveAccounts = accountStream
                .filter(Account::isMasterOrSlaveAccount)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_basic_predicate_use_case(Stream<Account> accountStream) {
        List<Account> masterAccounts = accountStream
                .filter(Account.Predicates.isAccountOfType("MST"))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneMasterAccounts = accountStream
                .filter(Account.Predicates.isAccountOfType("MST").negate())
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> masterOrSlaveAccounts = accountStream
                .filter(Account.Predicates.isAccountOfAnyTypes("MST", "SLV"))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void static_constant_predicate_use_case(Stream<Account> accountStream) {
        List<Account> masterAccounts = accountStream
                .filter(Account.Predicates.IS_MASTER)
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneMasterAccounts = accountStream
                .filter(Account.Predicates.IS_MASTER.negate())
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> masterOrSlaveAccounts = accountStream
                .filter(Account.Predicates.IS_MASTER_OR_SLAVE)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void dynamic_use_case(Stream<Account> accountStream, String... accountTypes) {
        List<Account> type0Accounts = accountStream
                .filter(account -> accountTypes[0].equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneType0Accounts = accountStream
                .filter(account -> !accountTypes[0].equals(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> selectedAccounts = accountStream
                .filter(account -> List.of(accountTypes).contains(account.getAccountType()))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void dynamic_basic_method_use_case(Stream<Account> accountStream, String... accountTypes) {
        List<Account> type0Accounts = accountStream
                .filter(account -> account.isAccountOf(accountTypes[0]))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneType0Accounts = accountStream
                .filter(account -> !account.isAccountOf(accountTypes[0]))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> selectedAccounts = accountStream
                .filter(account -> !account.isAccountOfAny(accountTypes))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static void dynamic_basic_predicate_use_case(Stream<Account> accountStream, String... accountTypes) {
        List<Account> type0Accounts = accountStream
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]))
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> noneType0Accounts = accountStream
                .filter(Account.Predicates.isAccountOfType(accountTypes[0]).negate())
                .peek(System.out::println)
                .collect(Collectors.toList());

        List<Account> selectedAccounts = accountStream
                .filter(Account.Predicates.isAccountOfAnyTypes(accountTypes))
                .peek(System.out::println)
                .collect(Collectors.toList());
    }
}
