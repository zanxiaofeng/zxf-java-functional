package zxf.java.functional.pattern.designpattern;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 装饰器模式的函数式实现，对应 Readme「函数式设计模式」。
 * 用 Function.andThen / compose 包装原函数实现装饰器，
 * 呼应 Readme 中 ILogDecorator.decorate 的命名柯里化风格。
 * 装饰器本身是一个高阶函数：接收原函数，返回包装后的新函数。
 */
public class DecoratorCases {

    public static void main(String[] args) {
        use_case1();
        use_case2();
    }

    // 日志装饰器：在原函数执行前后打印 —— 高阶函数，接收 target 返回包装后的新函数
    public static Function<String, String> logDecorator(Function<String, String> target) {
        return s -> {
            System.out.println("  [LOG before] input=" + s);
            String r = target.apply(s);
            System.out.println("  [LOG after]  result=" + r);
            return r;
        };
    }

    // 缓存装饰器：缓存原函数结果，重复输入不触发真实计算
    public static Function<String, String> cacheDecorator(Function<String, String> target) {
        Map<String, String> cache = new HashMap<>();
        return s -> cache.computeIfAbsent(s, target);
    }

    // use_case1: 用 andThen 链式叠加装饰（装饰器 = 一元函数链）
    public static void use_case1() {
        System.out.println("\n#case 1: 装饰器（Function.andThen 链式包装）");
        // 目标函数：转大写
        Function<String, String> upper = String::toUpperCase;
        // 装饰：upper -> 加尖括号 -> 加方括号
        Function<String, String> decorated = upper
                .andThen(s -> "<" + s + ">")
                .andThen(s -> "[" + s + "]");
        System.out.println("多层 andThen 装饰 'hi' => " + decorated.apply("hi"));
    }

    // use_case2: 命名柯里化风格装饰器，呼应 Readme ILogDecorator.decorate
    // decorate(target) 显式包装目标函数，缓存 + 日志层层叠加
    public static void use_case2() {
        System.out.println("\n#case 2: 命名装饰器（cacheDecorator + logDecorator 包装目标函数）");
        // 目标函数：模拟耗时计算
        Function<String, String> slowUpper = s -> {
            System.out.println("  (执行真实计算)");
            return s.toUpperCase();
        };

        // 先缓存装饰 => 再日志装饰，组合得到新函数
        Function<String, String> cached = cacheDecorator(slowUpper);
        Function<String, String> logged = logDecorator(cached);

        System.out.println("第1次调用 'abc':");
        String r1 = logged.apply("abc");
        System.out.println("  => " + r1);
        System.out.println("第2次调用 'abc'（命中缓存，无真实计算）:");
        String r2 = logged.apply("abc");
        System.out.println("  => " + r2);
    }
}
