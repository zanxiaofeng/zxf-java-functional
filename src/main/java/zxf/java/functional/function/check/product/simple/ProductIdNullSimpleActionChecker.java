package zxf.java.functional.function.check.product.simple;

import zxf.java.functional.core.checker.SimpleActionChecker;
import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Consumer;

public class ProductIdNullSimpleActionChecker extends SimpleActionChecker<Product> {
    private ProductIdNullSimpleActionChecker(Consumer<Product> trueAction, Consumer<Product> falseAction) {
        super(Product::isIdNull, trueAction, falseAction);
    }

    public static ProductIdNullSimpleActionChecker with(Consumer<Product> trueAction, Consumer<Product> falseAction){
        return new ProductIdNullSimpleActionChecker(trueAction, falseAction);
    }

    public static void main(String[] args) {
        ProductIdNullSimpleActionChecker productIdNullSimpleActionChecker = ProductIdNullSimpleActionChecker.with(
                p-> System.out.println("null"), product -> System.out.println("not null"));
        productIdNullSimpleActionChecker.checkAndAction(new Product());
    }
}
