package zxf.java.functional.pattern.closure.overview;

import java.util.List;
import java.util.function.Predicate;

public class AccountOverview {
    private List<Group> groups;

    public static Boolean checkAccountNumber(AccountOverview overview, String accountNumber) {
        return overview.groups.stream()
                .flatMap(group -> group.getAccounts().stream())
                .anyMatch(Predicate.isEqual(accountNumber));
    }

    public static Predicate<String> accountChecker(AccountOverview overview) {
        return (accountNumber) -> checkAccountNumber(overview, accountNumber);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Boolean checkAccount(String accountNumber) {
        return this.groups.stream()
                .flatMap(group -> group.getAccounts().stream())
                .anyMatch(Predicate.isEqual(accountNumber));
    }

    public static class Group {
        private String id;
        private String name;
        private List<Account> accounts;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Account> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<Account> accounts) {
            this.accounts = accounts;
        }
    }

    public static class Account {
        private String number;
        private String type;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
