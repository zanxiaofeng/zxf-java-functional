package zxf.java.functional.stream.account;

import java.util.stream.Stream;

public class AccountFactory {
    public static Account mstAccount() {
        return new Account("MST", "551001");
    }

    public static Account slvAccount() {
        return new Account("SLV", "331001");
    }

    public static Account othAccount() {
        return new Account("OTH", "111001");
    }

    public static Stream<Account> accountStream() {
        return Stream.of(new Account("MST", "551001"),
                new Account("MST", "551002"),
                new Account("SLV", "331001"),
                new Account("SLV", "331001"),
                new Account("OTH", "111001"));
    }
}
