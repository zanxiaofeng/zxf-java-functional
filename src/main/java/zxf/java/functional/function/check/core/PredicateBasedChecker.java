package zxf.java.functional.function.check.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateBasedChecker<T> {
    private final CheckRule<T>[] checkRules;

    protected PredicateBasedChecker(CheckRule<T>... checkRules) {
        this.checkRules = checkRules;
    }

    public String singleCheck(T checkObject) {
        for (int i = 0; i < checkRules.length; i++) {
            if (Arrays.stream(checkRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                return checkRules[i].result;
            }
        }
        return null;
    }

    public List<String> multipleCheck(T checkObject) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < checkRules.length; i++) {
            if (Arrays.stream(checkRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                result.add(checkRules[i].result);
            }
        }
        return result;
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
