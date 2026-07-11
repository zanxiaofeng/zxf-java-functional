package zxf.java.functional.function;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FunctionalUsage {
    public static void main(String[] args) {
        demoAsVariableType();
        demoAsParameterType();
        demoAsReturnType();
        demoCheckedExceptionWrap();
    }

    // 函数作为变量类型：Function<String, Integer> 是变量类型，Integer::parseInt 是函数值（方法引用）
    // 对应 Readme「函数作为类型和值 - 变量类型」
    public static void demoAsVariableType() {
        System.out.println("demoAsVariableType：函数作为变量类型");
        Function<String, Integer> parser = Integer::parseInt;
        System.out.println("  parser.apply(\"123\") = " + parser.apply("123"));
    }

    // 函数作为参数类型（高阶函数=入参）：apply 接收一个 Function 并执行它
    // 对应 Readme「函数式多态 - 函数作为入参的多态」
    public static void demoAsParameterType() {
        System.out.println("demoAsParameterType：函数作为参数类型（高阶函数=入参）");
        Integer length = apply(String::length, "hello");
        Integer parsed = apply(Integer::valueOf, "456");
        System.out.println("  apply(String::length, \"hello\") = " + length);
        System.out.println("  apply(Integer::valueOf, \"456\") = " + parsed);
    }

    // 函数作为返回值类型（高阶函数=返回值，补全「作返回值的多态」）：
    // 同一个方法根据入参 n 的不同，返回不同的 Predicate<String> 实例
    // 对应 Readme「函数式多态 - 函数作为返回值的多态（不同情况返回不同函数）」
    public static void demoAsReturnType() {
        System.out.println("demoAsReturnType：函数作为返回值类型（高阶函数=返回值）");
        Predicate<String> gt3 = lengthGt(3);
        Predicate<String> gt5 = lengthGt(5);
        System.out.println("  lengthGt(3).test(\"hi\") = " + gt3.test("hi"));
        System.out.println("  lengthGt(3).test(\"java\") = " + gt3.test("java"));
        System.out.println("  lengthGt(5).test(\"java\") = " + gt5.test("java"));
    }

    // 受检异常无法直接赋值给函数类型：
    // log 声明了 throws Exception，而 BiConsumer<Path,String>.accept 不声明 throws，
    // 因此 FunctionalUsage::log 不能作为 BiConsumer<Path,String> 的值。
    // 解决方案：用一个不抛受检异常的 lambda 包装（catch 后包装成 RuntimeException 重新抛出）。
    // 对应 Readme「Java函数式的特别」关于 Throws 子句覆盖包含的约束。
    public static void demoCheckedExceptionWrap() {
        System.out.println("demoCheckedExceptionWrap：受检异常的 wrap 解决方案");
        // 以下赋值无法编译（throws Exception 不在 BiConsumer.accept 的 throws 子句中）：
        // BiConsumer<Path, String> fileLogger = FunctionalUsage::log;
        // wrap：捕获受检异常并以非受检异常形式重新抛出，使函数值与函数类型兼容
        BiConsumer<Path, String> fileLogger = (path, message) -> {
            try {
                FunctionalUsage.log(path, message);
            } catch (Exception e) {
                throw new RuntimeException("写日志失败", e);
            }
        };
        System.out.println("  fileLogger 已成功构造（wrap 后类型兼容 BiConsumer<Path,String>）：" + fileLogger);
    }

    // 高阶函数：函数作为参数
    public static <T, R> R apply(Function<T, R> f, T t) {
        return f.apply(t);
    }

    // 高阶函数：函数作为返回值（不同 n 返回不同的 Predicate<String>）
    public static Predicate<String> lengthGt(int n) {
        return s -> s != null && s.length() > n;
    }

    public static void log(Path logFile, String message) throws Exception {
        Files.write(logFile, (message + "\n").getBytes(StandardCharsets.UTF_8), CREATE, APPEND);
    }
}
