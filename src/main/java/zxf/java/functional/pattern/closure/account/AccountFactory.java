package zxf.java.functional.pattern.closure.account;

import java.util.stream.Stream;

public class AccountFactory {
    public static Stream<Account> accountStream() {
        return Stream.of(new Account("MST", "551001"),
                new Account("MST", "551002"),
                new Account("SLV", "331001"),
                new Account("SLV", "331001"),
                new Account("OTH", "111001"));
    }
}
