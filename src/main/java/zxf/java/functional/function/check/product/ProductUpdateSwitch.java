package zxf.java.functional.function.check.product;

import zxf.java.functional.function.check.core.PredicateBasedSwitch;
import zxf.java.functional.function.check.product.model.ProductUpdateInput;

import java.util.function.Predicate;

public class ProductUpdateSwitch extends PredicateBasedSwitch<ProductUpdateInput> {
    public ProductUpdateSwitch() {
        super(
                new SwitchRule<>("error-1", ProductUpdateSwitch::errorAction, Predicate.not(ProductUpdateInput::preHasName), Predicate.not(ProductUpdateInput::postHasName)),
                new SwitchRule<>("error-2", ProductUpdateSwitch::errorAction, Predicate.not(ProductUpdateInput::preHasEmail), Predicate.not(ProductUpdateInput::postHasEmail))
        );
    }

    public static void errorAction(ProductUpdateInput productUpdateInput, String caseId) {
        System.out.println("action of " + caseId);
    }
}
