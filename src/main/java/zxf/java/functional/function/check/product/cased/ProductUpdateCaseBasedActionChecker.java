package zxf.java.functional.function.check.product.cased;

import zxf.java.functional.core.checker.CaseBasedActionChecker;
import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Predicate;

public class ProductUpdateCaseBasedActionChecker extends CaseBasedActionChecker<ProductUpdateInput> {
    public ProductUpdateCaseBasedActionChecker() {
        super(
                new CaseActionRule<>("error-1", ProductUpdateCaseBasedActionChecker::errorAction, Predicate.not(ProductUpdateInput::preHasName), Predicate.not(ProductUpdateInput::postHasName)),
                new CaseActionRule<>("error-2", ProductUpdateCaseBasedActionChecker::errorAction, Predicate.not(ProductUpdateInput::preHasEmail), Predicate.not(ProductUpdateInput::postHasEmail))
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
