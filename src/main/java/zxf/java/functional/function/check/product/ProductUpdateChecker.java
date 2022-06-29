package zxf.java.functional.function.check.product;

import zxf.java.functional.function.check.core.PredicateBasedChecker;
import zxf.java.functional.function.check.product.model.ProductUpdateInput;

import java.util.function.Predicate;

public class ProductUpdateChecker extends PredicateBasedChecker<ProductUpdateInput> {

    public ProductUpdateChecker() {
        super(
                new CheckRule<>("error-1", Predicate.not(ProductUpdateInput::preHasName), Predicate.not(ProductUpdateInput::postHasName)),
                new CheckRule<>("error-2", Predicate.not(ProductUpdateInput::preHasEmail), Predicate.not(ProductUpdateInput::postHasEmail))
        );
    }
}
