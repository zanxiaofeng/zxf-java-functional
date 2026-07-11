package zxf.java.functional.function.check.product.cased;

import zxf.java.functional.core.checker.CaseBasedChecker;
import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Predicate;

public class ProductUpdateCaseBasedChecker extends CaseBasedChecker<ProductUpdateInput> {

    public ProductUpdateCaseBasedChecker() {
        // 使用 Java 8 原生的 Predicate.negate() 做逻辑取反，与 Readme「Java8 函数式」定位一致。
        // 注意：Predicate.not() 是 Java 11+ 的增强 API，此处不使用。
        super(
                new CaseRule<>("error-1", ((Predicate<ProductUpdateInput>) ProductUpdateInput::preHasName).negate(), ((Predicate<ProductUpdateInput>) ProductUpdateInput::postHasName).negate()),
                new CaseRule<>("error-2", ((Predicate<ProductUpdateInput>) ProductUpdateInput::preHasEmail).negate(), ((Predicate<ProductUpdateInput>) ProductUpdateInput::postHasEmail).negate())
        );
    }

    public static void main(String[] args) {
        ProductUpdateCaseBasedChecker productUpdateCaseBasedChecker = new ProductUpdateCaseBasedChecker();
        System.out.println(productUpdateCaseBasedChecker.singleCheck(new ProductUpdateInput(new Product(), new Product())));
    }
}
