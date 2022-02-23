package zxf.java.functional.pattern.closure.account;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Account {
    private String accountType;
    private String accountNumber;

    public Account(String accountType, String accountNumber) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String toString() {
        return String.format("Account[%s, %s]", accountType, accountNumber);
    }

    public interface Predicates {
        //Predefined Predicate Constant for static usage
        Predicate<Account> IS_MASTER = isAccountOfType("MST");
        //Predefined Predicate Constant for static usage
        Predicate<Account> IS_MASTER_OR_SLAVE = isAccountOfAnyTypes("MST", "SLV");

        //Basic Predicate Method for static and dynamic use
        static Predicate<Account> isAccountOfType(String accountType) {
            return account -> Objects.equals(accountType, account.getAccountType());
        }

        //Basic Predicate Method for static and dynamic use
        static Predicate<Account> isAccountOfAnyTypes(String... accountTypes) {
            return account -> List.of(accountTypes).contains(account.getAccountType());
        }
    }
}