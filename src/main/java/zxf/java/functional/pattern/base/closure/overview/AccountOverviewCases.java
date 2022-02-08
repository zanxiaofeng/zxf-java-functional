package zxf.java.functional.pattern.base.closure.overview;

import zxf.java.functional.pattern.base.currying.Currying;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class AccountOverviewCases {

    public static void main(String[] args) {
        case_procedure();
        case_oop();
        case_functional_1();
        case_functional_2();
    }

    public static void case_procedure() {
        AccountOverview overview = produce_accountOverview();
        AccountOverview.checkAccountNumber(overview, "123456");
    }


    public static void case_oop() {
        AccountOverview overview = produce_accountOverview();
        overview.checkAccount("123456");
    }

    public static void case_functional_1() {
        AccountOverview overview = produce_accountOverview();
        Predicate<String> accountChecker = AccountOverview.accountChecker(overview);
        accountChecker.test("123456");
    }

    public static void case_functional_2() {
        AccountOverview overview = produce_accountOverview();
        Function<String, Boolean> accountChecker = Currying.currying(overview, AccountOverview::checkAccountNumber);
        accountChecker.apply("123456");
    }

    private static AccountOverview produce_accountOverview() {
        List<AccountOverview.Account> accounts = new ArrayList<>();
        AccountOverview.Account account1 = new AccountOverview.Account();
        account1.setNumber("1234");
        accounts.add(account1);
        AccountOverview.Account account2 = new AccountOverview.Account();
        account2.setNumber("12345");
        accounts.add(account2);
        AccountOverview.Account account3 = new AccountOverview.Account();
        account3.setNumber("12346");
        accounts.add(account3);

        AccountOverview.Group group = new AccountOverview.Group();
        group.setAccounts(accounts);

        AccountOverview overview = new AccountOverview();
        overview.setGroups(Collections.singletonList(group));
        return overview;
    }
}
