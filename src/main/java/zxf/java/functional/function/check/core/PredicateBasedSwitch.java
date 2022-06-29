package zxf.java.functional.function.check.core;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class PredicateBasedSwitch<T> {
    private final SwitchRule<T>[] switchRules;

    protected PredicateBasedSwitch(SwitchRule<T>... switchRules) {
        this.switchRules = switchRules;
    }

    public void run(T checkObject) {
        for (int i = 0; i < switchRules.length; i++) {
            if (Arrays.stream(switchRules[i].getCaseChecks()).allMatch(check -> check.test(checkObject))) {
                switchRules[i].caseAction.accept(checkObject, switchRules[i].caseId);
                break;
            }
        }
    }

    public static class SwitchRule<T> {
        private String caseId;
        private Predicate<T>[] caseChecks;
        private BiConsumer<T, String> caseAction;

        public SwitchRule(String caseId, BiConsumer<T, String> action, Predicate<T>... checks) {
            this.caseId = caseId;
            this.caseChecks = checks;
            this.caseAction = action;
        }

        public String getCaseId() {
            return caseId;
        }

        public Predicate<T>[] getCaseChecks() {
            return caseChecks;
        }

        public BiConsumer<T, String> getCaseAction() {
            return caseAction;
        }
    }
}
