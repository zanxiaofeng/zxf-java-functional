package zxf.java.functional.stream.account;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class AccountBasicCases {
    private static void static_use_case(Account account) {
        if ("MST".equals(account.getAccountType())) {
            System.out.println("do some thing for MST type");
        }

        if (!"MST".equals(account.getAccountType())) {
            System.out.println("do some thing for none-MST type");
        }

        if ("MST".equals(account.getAccountType())
                || "SLV".equals(account.getAccountType())) {
            System.out.println("do some thing for MST or SLV type");
        }
    }

    private static void static_basic_method_use_case(Account account) {
        if (account.isAccountOf("MST")) {
            System.out.println("do some thing for MST type");
        }

        if (!account.isAccountOf("MST")) {
            System.out.println("do some thing for none-MST type");
        }

        if (account.isAccountOfAny("MST", "SLV")) {
            System.out.println("do some thing for MST or SLV type");
        }
    }

    private static void static_convenience_method_use_case(Account account) {
        if (account.isMasterAccount()) {
            System.out.println("do some thing for MST type");
        }

        if (!account.isMasterAccount()) {
            System.out.println("do some thing for none-MST type");
        }

        if (account.isMasterOrSlaveAccount()) {
            System.out.println("do some thing for MST or SLV type");
        }
    }

    private static void static_basic_predicate_use_case(Account account) {
        if (Account.Predicates.isAccountOfType("MST").test(account)) {
            System.out.println("do some thing for MST type");
        }

        if (Account.Predicates.isAccountOfType("MST").negate().test(account)) {
            System.out.println("do some thing for none-MST type");
        }

        if (Account.Predicates.isAccountOfAnyTypes("MST", "SLV").test(account)) {
            System.out.println("do some thing for MST or SLV type");
        }
    }

    private static void static_constant_predicate_use_case(Account account) {
        if (Account.Predicates.IS_MASTER.test(account)) {
            System.out.println("do some thing for MST type");
        }

        if (Account.Predicates.IS_MASTER.negate().test(account)) {
            System.out.println("do some thing for none-MST type");
        }

        if (Account.Predicates.IS_MASTER_OR_SLAVE.test(account)) {
            System.out.println("do some thing for MST or SLV type");
        }
    }

    private static void dynamic_use_case(Account account, String... accountTypes) {
        if (accountTypes[0].equals(account.getAccountType())) {
            System.out.println(String.format("do some thing for %s type", accountTypes[0]));
        }

        if (!accountTypes[0].equals(account.getAccountType())) {
            System.out.println(String.format("do some thing for none-%s type", accountTypes[0]));
        }

        if (List.of(accountTypes).contains(account.getAccountType())) {
            System.out.println(String.format("do some thing for any of %s type", String.join(",", accountTypes)));
        }
    }

    private static void dynamic_basic_method_use_case(Account account, String... accountTypes) {
        if (account.isAccountOf(accountTypes[0])) {
            System.out.println(String.format("do some thing for %s type", accountTypes[0]));
        }

        if (!account.isAccountOf(accountTypes[0])) {
            System.out.println(String.format("do some thing for none-%s type", accountTypes[0]));
        }

        if (account.isAccountOfAny(accountTypes)) {
            System.out.println(String.format("do some thing for any of %s type", String.join(",", accountTypes)));
        }
    }

    private static void dynamic_basic_predicate_use_case(Account account, String... accountTypes) {
        if (Account.Predicates.isAccountOfType(accountTypes[0]).test(account)) {
            System.out.println(String.format("do some thing for %s type", accountTypes[0]));
        }

        if (!Account.Predicates.isAccountOfType(accountTypes[0]).test(account)) {
            System.out.println(String.format("do some thing for none-%s type", accountTypes[0]));
        }

        if (Account.Predicates.isAccountOfAnyTypes(accountTypes).test(account)) {
            System.out.println(String.format("do some thing for any of %s type", String.join(",", accountTypes)));
        }
    }
}
