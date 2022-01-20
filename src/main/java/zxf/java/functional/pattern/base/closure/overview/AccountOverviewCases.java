package zxf.java.functional.pattern.base.closure.overview;

import java.util.Arrays;
import java.util.function.Predicate;

public class AccountOverviewCases {

    public static void main(String[] args) {
        case_procedure();
        case_oop();
        case_functional();
    }

    public static void case_procedure() {
        AccountOverview overview = produce_accountOverview();
        businessProcessForProcedure(overview);
    }

    private static void businessProcessForProcedure(AccountOverview overview) {
        Arrays.stream(new String[]{"12345", "54321"}).allMatch(account ->
                AccountOverview.checkAccount(overview, account)
        );
    }

    public static void case_oop() {
        AccountOverview overview = produce_accountOverview();
        businessProcessForOOP(overview);
    }

    private static void businessProcessForOOP(AccountOverview overview) {
        Arrays.stream(new String[]{"12345", "54321"}).allMatch(account ->
                overview.checkAccount(account)
        );
    }

    public static void case_functional() {
        AccountOverview overview = produce_accountOverview();
        businessProcessForFunctional(overview.accountChecker());
    }

    private static void businessProcessForFunctional(Predicate<String> accountChecker) {
        Arrays.stream(new String[]{"12345", "54321"}).allMatch(accountChecker);
    }

    private static AccountOverview produce_accountOverview() {
        return null;
    }
}
