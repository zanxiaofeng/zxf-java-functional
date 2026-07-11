package zxf.java.functional.function;

import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaUsage {
    public static void main(String[] args) {
        use_case1_standardForm();
        use_case2_singleLineForm();
        use_case3_noParamForm();
        use_case4_multiStmtBlock();
    }

    // 标准写法：完整参数列表 + 花括号块 + 显式 return
    // 对应 Readme「Java中函数值的形式 - 立即函数 - 标准写法」
    public static void use_case1_standardForm() {
        System.out.println("use_case1 标准写法：(Product p) -> { return p.getId(); }");
        Function<Product, String> standardForm = (Product p) -> {
            return p.getId();
        };
        Product product = new Product();
        product.setId("P-001");
        System.out.println("  standardForm.apply 结果 = " + standardForm.apply(product));
    }

    // 单行写法：省略花括号与 return，表达式本身即返回值
    // 与 use_case1 等价：编译器对两种写法推导出的函数语义一致
    // 对应 Readme「Java中函数值的形式 - 立即函数 - 单行写法」
    public static void use_case2_singleLineForm() {
        System.out.println("use_case2 单行写法：(Product p) -> p.getId()（与标准写法等价）");
        Function<Product, String> singleLineForm = (Product p) -> p.getId();
        Product product = new Product();
        product.setId("P-002");
        System.out.println("  singleLineForm.apply 结果 = " + singleLineForm.apply(product));
    }

    // 无参 Lambda：() -> "hello"，对应 Supplier（无参数的函数类型）
    public static void use_case3_noParamForm() {
        System.out.println("use_case3 无参写法：() -> \"hello\"");
        Supplier<String> noParamForm = () -> "hello";
        System.out.println("  noParamForm.get 结果 = " + noParamForm.get());
    }

    // 多语句块：参数 -> { 语句1; 语句2; ...; return 结果; }
    // 块内可包含多条语句，必须以显式 return 返回结果
    public static void use_case4_multiStmtBlock() {
        System.out.println("use_case4 多语句块 + 显式 return");
        Function<Product, String> multiStmtBlock = (Product p) -> {
            String prefix = "[Product]";
            String id = p.getId();
            return prefix + id;
        };
        Product product = new Product();
        product.setId("P-004");
        System.out.println("  multiStmtBlock.apply 结果 = " + multiStmtBlock.apply(product));
    }
}
