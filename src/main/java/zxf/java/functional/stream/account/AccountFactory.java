package zxf.java.functional.stream.account;

import zxf.java.functional.pattern.closure.account.Account;

import java.util.stream.Stream;

public class AccountFactory {
    public static Account mstAccount() {
        return new Account("MST", "551001");
    }

    public static Account slvAccount() {
        return new Account("SLV", "331001");
    }

    public static Account othAccount() {
        return new Account("MST", "111001");
    }

    public static Stream<zxf.java.functional.pattern.closure.account.Account> accountStream() {
        return Stream.of(new Account("MST", "551001"),
                new Account("MST", "551002"),
                new Account("SLV", "331001"),
                new Account("SLV", "331001"),
                new Account("MST", "111001"));
    }
}
