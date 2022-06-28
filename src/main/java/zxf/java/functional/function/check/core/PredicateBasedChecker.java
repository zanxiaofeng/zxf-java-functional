package zxf.java.functional.function.check.core;

import java.util.Arrays;
import java.util.function.Predicate;

public class PredicateBasedChecker<T> {
    private final CheckRule<T>[] checkRules;

    protected PredicateBasedChecker(CheckRule<T>... checkRules) {
        this.checkRules = checkRules;
    }

    public String check(T checkObject) {
        for (int i = 0; i < checkRules.length; i++) {
            if (Arrays.stream(checkRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                return checkRules[i].result;
            }
        }
        return null;
    }

    public static class CheckRule<T> {
        private String result;
        private Predicate<T>[] checks;

        public CheckRule(String result, Predicate<T>... checks) {
            this.result = result;
            this.checks = checks;
        }

        public String getResult() {
            return result;
        }

        public Predicate<T>[] getChecks() {
            return checks;
        }
    }
}
