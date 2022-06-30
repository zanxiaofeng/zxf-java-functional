package zxf.java.functional.function.check.product.simple;

import zxf.java.functional.core.checker.SimpleChecker;
import zxf.java.functional.function.check.product.model.Product;

public class ProductIdNullSimpleChecker extends SimpleChecker<Product> {
    private ProductIdNullSimpleChecker(Product checkObject) {
        super(Product::isIdNull, checkObject);
    }

    public static ProductIdNullSimpleChecker with(Product product) {
        return new ProductIdNullSimpleChecker(product);
    }

    public static void main(String[] args) {
        ProductIdNullSimpleChecker productIdNullSimpleChecker = ProductIdNullSimpleChecker.with(new Product());
        productIdNullSimpleChecker.ifTrueOrFalse(p-> System.out.println("true"),
                p-> System.out.println("false"));
    }
}
