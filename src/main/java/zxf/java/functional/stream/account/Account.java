package zxf.java.functional.stream.account;

import java.util.Arrays;
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

    //Basic Method for static and dynamic usage
    public Boolean isAccountOf(String accountType) {
        Objects.requireNonNull(accountType);
        return accountType.equalsIgnoreCase(getAccountType());
    }

    //Basic Method for static and dynamic usage
    public Boolean isAccountOfAny(String... accountTypes) {
        Objects.requireNonNull(accountTypes);
        return Arrays.stream(accountTypes).anyMatch(this::isAccountOf);
    }

    //Convenience Method for static usage
    public Boolean isMasterAccount() {
        return isAccountOf("MST");
    }

    //Convenience Method for static usage
    public Boolean isMasterOrSlaveAccount() {
        return isAccountOfAny("MST", "SLV");
    }


    public interface Predicates {
        //Predefined Predicate Constant for static usage
        Predicate<Account> IS_MASTER = isAccountOfType("MST");
        //Predefined Predicate Constant for static usage
        Predicate<Account> IS_MASTER_OR_SLAVE = isAccountOfAnyTypes("MST", "SLV");

        //Basic Predicate Method for static and dynamic use
        static Predicate<Account> isAccountOfType(String accountType) {
            return account -> account.isAccountOf(accountType);
        }

        //Basic Predicate Method for static and dynamic use
        static Predicate<Account> isAccountOfAnyTypes(String... accountTypes) {
            return account -> account.isAccountOfAny(accountTypes);
        }
    }
}