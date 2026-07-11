package zxf.java.functional.function.check.product.cased;

import zxf.java.functional.core.checker.CaseBasedActionChecker;
import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Predicate;

public class ProductUpdateCaseBasedActionChecker extends CaseBasedActionChecker<ProductUpdateInput> {
    public ProductUpdateCaseBasedActionChecker() {
        // 使用 Java 8 原生的 Predicate.negate() 做逻辑取反，与 Readme「Java8 函数式」定位一致。
        // 注意：Predicate.not() 是 Java 11+ 的增强 API，此处不使用。
        super(
                new CaseActionRule<>("error-1", ProductUpdateCaseBasedActionChecker::errorAction, ((Predicate<ProductUpdateInput>) ProductUpdateInput::preHasName).negate(), ((Predicate<ProductUpdateInput>) ProductUpdateInput::postHasName).negate()),
                new CaseActionRule<>("error-2", ProductUpdateCaseBasedActionChecker::errorAction, ((Predicate<ProductUpdateInput>) ProductUpdateInput::preHasEmail).negate(), ((Predicate<ProductUpdateInput>) ProductUpdateInput::postHasEmail).negate())
        );
    }

    private static void errorAction(ProductUpdateInput productUpdateInput, String caseId) {
        System.out.println("action of " + caseId);
    }

    public static void main(String[] args) {
        ProductUpdateCaseBasedActionChecker productUpdateCaseBasedActionChecker = new ProductUpdateCaseBasedActionChecker();
        productUpdateCaseBasedActionChecker.multipleCheckAndAction(new ProductUpdateInput(new Product(), new Product()));
    }
}
