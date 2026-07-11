package zxf.java.functional.pattern.hof;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 高阶函数 (Higher-Order Function) 演示。
 * 对应 Readme「函数式多态（Java）」章节：函数作入参 / 作返回值 / 作类成员变量形成组合式多态。
 */
public class HOFCases {

    public static void main(String[] args) {
        use_case1();
        use_case2();
        use_case3();
    }

    // 函数作为入参的多态：同一个 apply 可传入不同函数得到不同行为
    public static <T, R> R apply(Function<T, R> f, T t) {
        return f.apply(t);
    }

    // 函数作入参的多态：将 Function 作为参数，同一个骨架可复用于多种转换
    public static void use_case1() {
        System.out.println("\n#case 1: 函数作为入参的多态（同一个 apply，不同函数 => 不同行为）");
        System.out.println("apply(String::length, \"hello\") = " + apply(String::length, "hello"));
        System.out.println("apply(String::toUpperCase, \"hello\") = " + apply(String::toUpperCase, "hello"));
        System.out.println("apply((Integer x) -> x * x, 6) = " + apply((Integer x) -> x * x, 6));
    }

    // 函数作为返回值的多态：不同 n 返回不同的判断函数
    public static Predicate<String> lengthGt(int n) {
        return s -> s != null && s.length() > n;
    }

    // 函数作为返回值的多态：lengthGt 根据入参 n 生成不同的 Predicate
    public static void use_case2() {
        System.out.println("\n#case 2: 函数作为返回值的多态（不同 n => 不同函数）");
        List<String> words = Arrays.asList("a", "abc", "hello", "functional");
        Predicate<String> gt3 = lengthGt(3);
        Predicate<String> gt5 = lengthGt(5);
        System.out.println("lengthGt(3) 结果: " + words.stream().filter(gt3).toList());
        System.out.println("lengthGt(5) 结果: " + words.stream().filter(gt5).toList());
    }

    // 以组合（持有函数字段）而非继承的方式实现多态：不同的 processor 持有不同的处理函数
    public static class FunctionalProcessor<T, R> {
        // 函数作为类成员变量：组合式多态的核心，对象行为由持有的函数决定
        private final Function<T, R> action;
        private final String name;

        public FunctionalProcessor(String name, Function<T, R> action) {
            this.name = name;
            this.action = action;
        }

        public R process(T input) {
            return action.apply(input);
        }

        public String name() {
            return name;
        }
    }

    // 函数作为类成员变量形成组合式多态：用持有不同函数字段的实例替代子类继承
    public static void use_case3() {
        System.out.println("\n#case 3: 函数作为类成员变量形成组合式多态（组合而非继承）");
        FunctionalProcessor<String, Integer> lengthProcessor = new FunctionalProcessor<>("取长度", String::length);
        FunctionalProcessor<String, String> upperProcessor = new FunctionalProcessor<>("转大写", String::toUpperCase);
        FunctionalProcessor<Integer, Integer> squareProcessor = new FunctionalProcessor<>("平方", x -> x * x);

        String input = "Hello";
        System.out.println(lengthProcessor.name() + "(\"" + input + "\") = " + lengthProcessor.process(input));
        System.out.println(upperProcessor.name() + "(\"" + input + "\") = " + upperProcessor.process(input));
        System.out.println(squareProcessor.name() + "(5) = " + squareProcessor.process(5));
    }
}
