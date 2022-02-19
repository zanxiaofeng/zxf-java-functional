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

    //基础方法
    public Boolean isAccountOf(String accountType) {
        Objects.requireNonNull(accountType);
        return accountType.equalsIgnoreCase(getAccountType());
    }

    //基础方法
    public Boolean isAccountOfAny(String... accountTypes) {
        return Arrays.stream(accountTypes).anyMatch(this::isAccountOf);
    }

    //快捷方法
    public Boolean isMasterAccount() {
        return isAccountOf("MST");
    }

    //快捷方法
    public Boolean isSlaveAccount() {
        return isAccountOf("SLV");
    }

    //快捷方法
    public Boolean isOtherAccount() {
        return isAccountOf("OTH");
    }

    //快捷方法
    public Boolean isMasterOrSlaveAccount() {
        return isAccountOfAny("MST", "SLV");
    }

    //快捷方法
    public Boolean isMasterOrOtherAccount() {
        return isAccountOfAny("MST", "OTH");
    }

    //快捷方法
    public Boolean isSlaveOrOtherAccount() {
        return isAccountOfAny("SLV", "OTH");
    }

    public interface Predicates {
        //Lambda快捷常量
        Predicate<Account> IS_MASTER = isAccountOfType("MST");
        Predicate<Account> IS_SLAVE = isAccountOfType("SLV");
        Predicate<Account> IS_OTHER = isAccountOfType("OTH");
        Predicate<Account> IS_MASTER_OR_SLAVE = isAccountOfAnyTypes("MST", "SLV");
        Predicate<Account> IS_MASTER_OR_OTHER = isAccountOfAnyTypes("MST", "OTH");
        Predicate<Account> IS_SLAVE_OR_OTHER = isAccountOfAnyTypes("SLV", "OTH");

        //Lambda支持方法
        static Predicate<Account> isAccountOfType(String accountType) {
            return account -> account.isAccountOf(accountType);
        }

        //Lambda支持方法
        static Predicate<Account> isAccountOfAnyTypes(String... accountTypes) {
            return account -> account.isAccountOfAny(accountTypes);
        }
    }
}