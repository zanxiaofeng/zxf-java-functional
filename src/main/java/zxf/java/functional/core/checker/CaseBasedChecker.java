package zxf.java.functional.core.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CaseBasedChecker<T> {
    private final CaseRule<T>[] checkRules;

    protected CaseBasedChecker(CaseRule<T>... checkRules) {
        this.checkRules = checkRules;
    }

    /** 返回第一个全部条件命中的规则 caseId；无命中时返回 {@link Optional#empty()}（不再用 null 表示无结果）。 */
    public Optional<String> singleCheck(T checkObject) {
        for (int i = 0; i < checkRules.length; i++) {
            if (Arrays.stream(checkRules[i].getChecks()).allMatch(check -> check.test(checkObject))) {
                return Optional.of(checkRules[i].getCaseId());
            }
        }
        return Optional.empty();
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

        /** 返回条件数组的防御性拷贝，防止外部篡改规则。 */
        public Predicate<T>[] getChecks() {
            return checks.clone();
        }
    }
}
