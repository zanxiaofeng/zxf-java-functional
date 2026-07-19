package zxf.java.functional.function.check.product.cased;

import zxf.java.functional.core.checker.CaseBasedChecker;
import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Predicate;

public class ProductUpdateCaseBasedChecker extends CaseBasedChecker<ProductUpdateInput> {

    public ProductUpdateCaseBasedChecker() {
        // 用 Predicate.not()（Java 11+）对方法引用做逻辑取反；项目已是 JDK 21，
        // 无需再用「显式强转 + negate()」的 Java 8 写法。
        super(
                new CaseRule<>("error-1", Predicate.not(ProductUpdateInput::preHasName), Predicate.not(ProductUpdateInput::postHasName)),
                new CaseRule<>("error-2", Predicate.not(ProductUpdateInput::preHasEmail), Predicate.not(ProductUpdateInput::postHasEmail))
        );
    }

    public static void main(String[] args) {
        ProductUpdateCaseBasedChecker productUpdateCaseBasedChecker = new ProductUpdateCaseBasedChecker();
        System.out.println("singleCheck 结果 = " + productUpdateCaseBasedChecker.singleCheck(new ProductUpdateInput(new Product(), new Product())));
    }
}
