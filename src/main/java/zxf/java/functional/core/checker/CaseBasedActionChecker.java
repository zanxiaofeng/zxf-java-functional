package zxf.java.functional.core.checker;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CaseBasedActionChecker<T> {
    private final CaseActionRule<T>[] caseActionRules;

    protected CaseBasedActionChecker(CaseActionRule<T>... caseActionRules) {
        this.caseActionRules = caseActionRules;
    }

    public void singleCheckAndAction(T checkObject) {
        for (int i = 0; i < caseActionRules.length; i++) {
            if (Arrays.stream(caseActionRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                caseActionRules[i].getAction().accept(checkObject, caseActionRules[i].getCaseId());
                break;
            }
        }
    }

    public void multipleCheckAndAction(T checkObject) {
        for (int i = 0; i < caseActionRules.length; i++) {
            if (Arrays.stream(caseActionRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                caseActionRules[i].getAction().accept(checkObject, caseActionRules[i].getCaseId());
            }
        }
    }

    public static class CaseActionRule<T> {
        private final String caseId;
        private final Predicate<T>[] checks;
        private final BiConsumer<T, String> action;

        public CaseActionRule(String caseId, BiConsumer<T, String> action, Predicate<T>... checks) {
            this.caseId = caseId;
            this.checks = checks;
            this.action = action;
        }

        public String getCaseId() {
            return caseId;
        }

        public Predicate<T>[] getChecks() {
            return checks;
        }

        public BiConsumer<T, String> getAction() {
            return action;
        }
    }
}
