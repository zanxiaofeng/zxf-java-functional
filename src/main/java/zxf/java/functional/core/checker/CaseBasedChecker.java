package zxf.java.functional.core.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CaseBasedChecker<T> {
    private final CaseRule<T>[] checkRules;

    protected CaseBasedChecker(CaseRule<T>... checkRules) {
        this.checkRules = checkRules;
    }

    public String singleCheck(T checkObject) {
        for (int i = 0; i < checkRules.length; i++) {
            if (Arrays.stream(checkRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                return checkRules[i].getCaseId();
            }
        }
        return null;
    }

    public List<String> multipleCheck(T checkObject) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < checkRules.length; i++) {
            if (Arrays.stream(checkRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                result.add(checkRules[i].getCaseId());
            }
        }
        return result;
    }

    public static class CaseRule<T> {
        private final String caseId;
        private final Predicate<T>[] checks;

        public CaseRule(String caseId, Predicate<T>... checks) {
            this.caseId = caseId;
            this.checks = checks;
        }

        public String getCaseId() {
            return caseId;
        }

        public Predicate<T>[] getChecks() {
            return checks;
        }
    }
}
