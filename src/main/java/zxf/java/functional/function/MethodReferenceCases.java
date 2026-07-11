package zxf.java.functional.function;

import zxf.java.functional.function.check.product.model.Product;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// 对应 Readme「方法引用」四种形式，每种给出等价 Lambda 并列对比。
public class MethodReferenceCases {
    public static void main(String[] args) {
        use_case1_staticMethod();
        use_case2_classInstanceMethod();
        use_case3_constructor();
        use_case4_objectInstanceMethod();
    }

    // Class::staticMethod（类静态方法引用）：Integer::valueOf
    // 等价 Lambda：s -> Integer.valueOf(s)
    // 对应 Readme「方法引用 - Class::staticMethod(类静态方法引用)」
    public static void use_case1_staticMethod() {
        System.out.println("use_case1 Class::staticMethod —— Integer::valueOf");
        Function<String, Integer> ref = Integer::valueOf;
        Function<String, Integer> lambda = s -> Integer.valueOf(s);
        System.out.println("  方法引用 apply(\"123\") = " + ref.apply("123"));
        System.out.println("  等价 Lambda apply(\"123\") = " + lambda.apply("123"));
    }

    // Class::instanceMethod（类实例方法引用）：String::length
    // 等价 Lambda：s -> s.length()（函数类型的第一个参数成为该方法的接收者）
    // 对应 Readme「方法引用 - Class::instanceMethod(类实例方法引用)」
    public static void use_case2_classInstanceMethod() {
        System.out.println("use_case2 Class::instanceMethod —— String::length");
        Function<String, Integer> ref = String::length;
        Function<String, Integer> lambda = s -> s.length();
        System.out.println("  方法引用 apply(\"hello\") = " + ref.apply("hello"));
        System.out.println("  等价 Lambda apply(\"hello\") = " + lambda.apply("hello"));
    }

    // Class::new（构造器引用）：Product::new
    // 等价 Lambda：() -> new Product()
    // 对应 Readme「方法引用 - Class::new(类构造器引用)」
    public static void use_case3_constructor() {
        System.out.println("use_case3 Class::new —— Product::new（构造器引用）");
        Supplier<Product> ref = Product::new;
        Supplier<Product> lambda = () -> new Product();
        System.out.println("  方法引用 get() = " + ref.get());
        System.out.println("  等价 Lambda get() = " + lambda.get());
    }

    // object::instanceMethod（对象实例方法引用）：System.out::println
    // 等价 Lambda：s -> System.out.println(s)
    // 对应 Readme「方法引用 - object:instanceMethod(对象实例方法引用)」
    public static void use_case4_objectInstanceMethod() {
        System.out.println("use_case4 object::instanceMethod —— System.out::println");
        Consumer<String> ref = System.out::println;
        Consumer<String> lambda = s -> System.out.println(s);
        System.out.print("  方法引用 accept(\"A\") => ");
        ref.accept("A");
        System.out.print("  等价 Lambda accept(\"B\") => ");
        lambda.accept("B");
    }
}
