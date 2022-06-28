package zxf.java.functional.function.check.product.checker;

import zxf.java.functional.function.check.core.PredicateBasedChecker;
import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Predicate;

public class ProductUpdateChecker extends PredicateBasedChecker<ProductUpdateChecker.ProductUpdateInput> {

    public ProductUpdateChecker() {
        super(
                new CheckRule<>("error-1", Predicate.not(ProductUpdateInput::preHasName), Predicate.not(ProductUpdateInput::postHasName)),
                new CheckRule<>("error-2", Predicate.not(ProductUpdateInput::preHasEmail), Predicate.not(ProductUpdateInput::postHasEmail))
        );
    }

    public static class ProductUpdateInput {
        private final Product preProduct;
        private final Product postProduct;

        public ProductUpdateInput(Product preProduct, Product postProduct) {
            this.preProduct = preProduct;
            this.postProduct = postProduct;
        }

        public boolean preHasName() {
            return preProduct.getName() != null;
        }

        public boolean postHasName() {
            return postProduct.getName() != null;
        }

        public boolean preHasEmail() {
            return preProduct.getEmail() != null;
        }

        public boolean postHasEmail() {
            return postProduct.getEmail() != null;
        }
    }
}
