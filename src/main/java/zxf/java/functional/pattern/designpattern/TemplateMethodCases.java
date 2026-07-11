package zxf.java.functional.pattern.designpattern;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 模板方法模式的函数式实现，对应 Readme「函数式设计模式」。
 * 用 Function/Consumer 组合替代抽象类继承：骨架固定，可变步骤作为函数入参注入。
 * 对比 OOP：OOP 需定义抽象类 + 子类重写 hook 方法；函数式只需传一个 Function。
 */
public class TemplateMethodCases {

    public static void main(String[] args) {
        use_case1();
    }

    // 函数式模板方法：骨架固定（before -> transform -> after），transform 步骤作为 Function 入参注入
    // 一个骨架方法可复用于任意 transform 策略，无需为每种变体新建子类
    public static <T> T processTemplate(T input,
                                        Consumer<T> before,
                                        Function<T, T> transform,
                                        Consumer<T> after) {
        before.accept(input);
        T result = transform.apply(input);
        after.accept(result);
        return result;
    }

    // 模板方法：骨架固定 + 函数式钩子（可变步骤作为 Function/Consumer 入参）
    public static void use_case1() {
        System.out.println("\n#case 1: 模板方法（Function/Consumer 组合替代抽象类继承）");
        String data = "  Hello Functional Template  ";

        // 变体 A：trim + 大写
        String trimmed = processTemplate(data,
                s -> System.out.println("  [before] input=" + s),
                s -> s.trim().toUpperCase(),
                s -> System.out.println("  [after]  result=" + s));
        System.out.println("trim+upper 结果: " + trimmed);

        System.out.println("---");

        // 变体 B：trim + 元音掩码 —— 复用同一骨架，仅换 transform
        String masked = processTemplate(data,
                s -> System.out.println("  [before] input=" + s),
                s -> s.trim().replaceAll("[aeiouAEIOU]", "*"),
                s -> System.out.println("  [after]  result=" + s));
        System.out.println("掩码元音 结果: " + masked);
    }
}
