package zxf.java.functional.function.check.product;

import zxf.java.functional.function.check.product.checker.ProductUpdateChecker;
import zxf.java.functional.function.check.product.model.Product;

public class Tests {
    public static void main(String[] args) {
        ProductUpdateChecker productUpdateChecker = new ProductUpdateChecker();
        System.out.println(productUpdateChecker.check(prepareProductUpdateInput()));
    }

    private static ProductUpdateChecker.ProductUpdateInput prepareProductUpdateInput() {
        return new ProductUpdateChecker.ProductUpdateInput(new Product(), new Product());
    }
}
