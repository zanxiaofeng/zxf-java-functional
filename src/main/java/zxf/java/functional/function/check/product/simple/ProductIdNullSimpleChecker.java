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
        // 分别构造 id=null 与 id="1" 两个 Product，演示 true/false 两个分支
        Product idNull = new Product();              // id 默认 null -> 命中 isIdNull，走 true 分支
        Product idPresent = new Product();
        idPresent.setId("1");                        // id 非 null -> 走 false 分支
        ProductIdNullSimpleChecker.with(idNull).ifTrueOrFalse(
                p -> System.out.println("true: id 为 null"),
                p -> System.out.println("false: id 非空"));
        ProductIdNullSimpleChecker.with(idPresent).ifTrueOrFalse(
                p -> System.out.println("true: id 为 null"),
                p -> System.out.println("false: id 非空"));
    }
}
