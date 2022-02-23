package zxf.java.functional.pattern.closure.account;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountCases {
    private static void basic_predicate_static_use_case(Stream<Account> accountStream) {
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

    private static void constant_predicate_static_use_case(Stream<Account> accountStream) {
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

    private static void basic_predicate_dynamic_use_case(Stream<Account> accountStream, String... accountTypes) {
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
