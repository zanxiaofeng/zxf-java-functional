package zxf.java.functional.core.optional;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.OptionalCurrying;
import zxf.java.functional.core.function.checked.CheckedFunction;

import java.util.function.Function;

/**
 * Optional 教学演示，对应 Readme 中「Optional / 幽灵类型 / Functor / Monad / Applicative」概念。
 *
 * <p>用 {@link Optional#of}/{@link Optional#ofNullable}/{@link Optional#empty} 工厂方法构造实例，
 * 演示 map/flatMap/apply（函数式变换）以及 filter/ifPresent/orElse/orElseGet/or（实用 API）。</p>
 */
public class OptionalCases {
    public static void main(String[] args) throws Exception {
        use_case1();
        use_case2();
        use_case3();
        use_case4();
        use_case5_factory_and_practical_api();
        use_case6_empty_get_throws();
    }

    //Functor：map 对有值/空值的行为
    public static void use_case1() {
        System.out.println("use_case1 Functor map");
        Optional<Integer> optional999 = Optional.of(999);
        Optional<String> optional999Add10 = optional999.map(OptionalCases::add10);
        System.out.println(optional999Add10.get());

        // 空 Optional 调 map 仍是 empty，用 orElse 取值（不再用 get 返回 null）
        Optional<Integer> empty = Optional.empty();
        String safe = empty.map(OptionalCases::add10).orElse("(空)");
        System.out.println(safe);
    }

    //Monad：flatMap 串联可能为空的计算
    public static void use_case2() {
        System.out.println("use_case2 Monad flatMap");
        Optional<Integer> optional999 = Optional.of(999);
        Optional<String> optional999Add100 = optional999.flatMap(OptionalCases::add100);
        System.out.println(optional999Add100.get());

        Optional<Integer> optionalNull = Optional.empty();
        String safe = optionalNull.flatMap(OptionalCases::add100).orElse("(空)");
        System.out.println(safe);
    }

    //Applicative--左结合，Val->Func->*Func by apply val*->Val, 第一个参数用函子map，后面的参数用应用子apply
    public static void use_case3() throws Exception {
        System.out.println("use_case3 Applicative 左结合");
        Function<Integer, CheckedFunction<Integer, String>> curriedBiFunction = Currying.curryingFunction(OptionalCases::mulAndToString);

        Optional<Integer> optional10 = Optional.of(10);
        Optional<Integer> optional999 = Optional.of(999);
        Optional<String> result = optional999.applyChecked(optional10.map(curriedBiFunction));
        System.out.println(result.get());
    }

    //右结合, *Func by apply val*->Val
    public static void use_case4() throws Exception {
        System.out.println("use_case4 Applicative 右结合");
        OptionalCurrying.BiCurryingFunction<Integer, Integer, String> curriedBiFunction = OptionalCurrying.curryingFunction(OptionalCases::mulAndToString);

        Optional<Integer> optional10 = Optional.of(10);
        Optional<Integer> optional999 = Optional.of(999);
        Optional<String> result = curriedBiFunction.apply(optional10).apply(optional999);
        System.out.println(result.get());
    }

    //工厂方法 + 实用 API：of / ofNullable / empty / filter / ifPresent / orElse / orElseGet / or
    public static void use_case5_factory_and_practical_api() {
        System.out.println("use_case5 工厂方法与实用 API");

        // of 拒绝 null（会抛 NPE）；ofNullable 同时接受 null 与非 null
        Optional<String> present = Optional.ofNullable("hello");
        Optional<String> absent = Optional.ofNullable(null);
        System.out.println("ofNullable(非空) = " + present + ", ofNullable(null) = " + absent);

        // filter：谓词过滤
        Optional<Integer> big = Optional.of(100).filter(i -> i > 50);
        Optional<Integer> small = Optional.of(10).filter(i -> i > 50);
        System.out.println("filter(>50) on 100 = " + big + ", on 10 = " + small);

        // ifPresent：仅在有值时执行副作用
        System.out.print("ifPresent 输出: ");
        Optional.of("data").ifPresent(s -> System.out.println(s));
        Optional.<String>empty().ifPresent(s -> System.out.println("这行不会打印"));

        // orElse：提供默认值
        System.out.println("orElse 默认值 = " + absent.orElse("默认"));

        // orElseGet：惰性求默认值（supplier 仅在空时调用）
        System.out.println("orElseGet 默认值 = " + absent.orElseGet(() -> "惰性默认"));

        // or：空时切换到另一个 Optional
        Optional<String> fallback = absent.or(() -> Optional.of("备用值"));
        System.out.println("or 备用 = " + fallback);
    }

    //语义修正：empty 的 get() 现抛 NoSuchElementException（与 JDK 一致）
    public static void use_case6_empty_get_throws() {
        System.out.println("use_case6 empty.get() 抛 NoSuchElementException");
        Optional<String> empty = Optional.empty();
        try {
            empty.get();
            System.out.println("ERROR: 应当抛异常");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("正确捕获: " + e.getMessage());
        }
    }

    public static String add10(Integer value) {
        return String.valueOf(value + 10);
    }

    public static Optional<String> add100(Integer value) {
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(String.valueOf(value + 100));
    }

    public static String mulAndToString(Integer left, Integer right) {
        String result = String.valueOf(left * right);
        System.out.println(String.format("%d * %d = %s", left, right, result));
        return result;
    }
}
