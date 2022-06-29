package zxf.java.functional.function.check.product;

import zxf.java.functional.function.check.product.model.Product;
import zxf.java.functional.function.check.product.model.ProductUpdateInput;

public class Tests {
    public static void main(String[] args) {
        ProductUpdateChecker productUpdateChecker = new ProductUpdateChecker();
        System.out.println(productUpdateChecker.singleCheck(prepareProductUpdateInput()));
        ProductUpdateSwitch productUpdateSwitch = new ProductUpdateSwitch();
        productUpdateSwitch.run(prepareProductUpdateInput());
    }

    private static ProductUpdateInput prepareProductUpdateInput() {
        return new ProductUpdateInput(new Product(), new Product());
    }
}
