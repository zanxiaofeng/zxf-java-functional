package zxf.java.functional.pattern.currying;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.Partial;
import zxf.java.functional.core.function.checked.CheckedBiFunction;
import zxf.java.functional.core.function.checked.CheckedFunction;
import zxf.java.functional.core.function.checked.CheckedTriFunction;

import java.util.function.Function;

/**
 * 柯里化与偏应用演示，对应 Readme「函数式的基本模式 - 柯里化 / 偏应用 / 命名柯里化」。
 * 柯里化：把多参函数拆成一连串单参函数；
 * 偏应用：固定多参函数的部分参数得到新函数；
 * 命名柯里化：给每个单参步骤一个显式方法名（如 decorate(level) 返回单参 logger）。
 */
public class CurryingCases {

    public static void main(String[] args) throws Exception {
        use_case1();
        use_case2();
        use_case3();
        use_case4();
    }

    // 基础二元函数
    public static Integer addBi(Integer x, Integer y) {
        return x + y;
    }

    // 基础三元函数（已修复：原 addTri 返回 x+y，漏加 z）
    public static Integer addTri(Integer x, Integer y, Integer z) {
        return x + y + z;
    }

    // use_case1: 偏应用（core.Partial）—— 固定部分参数得到新函数
    public static void use_case1() throws Exception {
        System.out.println("\n#case 1: 偏应用（Partial.partialFunction 固定首参）");
        CheckedFunction<Integer, Integer> add4 = Partial.partialFunction(4, CurryingCases::addBi);
        System.out.println("partial(4, addBi).apply(5) = " + add4.apply(5));
        CheckedFunction<Integer, Integer> add10 = Partial.partialFunction(10, CurryingCases::addBi);
        System.out.println("partial(10, addBi).apply(5) = " + add10.apply(5));
    }

    // use_case2: 柯里化（core.Currying.curryingFunction）—— 多参函数拆成嵌套单参函数
    public static void use_case2() throws Exception {
        System.out.println("\n#case 2: 柯里化（Currying.curryingFunction 把 BiFunction/TriFunction 拆成嵌套 Function）");

        // 二元柯里化：x -> y -> result
        Function<Integer, CheckedFunction<Integer, Integer>> curriedAdd =
                Currying.curryingFunction((CheckedBiFunction<Integer, Integer, Integer>) CurryingCases::addBi);
        CheckedFunction<Integer, Integer> add5 = curriedAdd.apply(5);
        System.out.println("curriedAdd.apply(5).apply(7) = " + add5.apply(7));

        // 三元柯里化：x -> y -> z -> result（已修复 addTri 漏加 z 的 bug）
        Function<Integer, Function<Integer, CheckedFunction<Integer, Integer>>> curriedAddTri =
                Currying.curryingFunction((CheckedTriFunction<Integer, Integer, Integer, Integer>) CurryingCases::addTri);
        Integer triResult = curriedAddTri.apply(1).apply(2).apply(3);
        System.out.println("curriedAddTri.apply(1).apply(2).apply(3) = " + triResult);
    }

    // use_case3: 命名柯里化 —— logger(level) 返回单参 Function<String,String>
    // 给每个单参步骤一个显式方法名，提升可读性，避免一堆裸 lambda
    public static Function<String, String> logger(String level) {
        return message -> "[" + level + "] " + message;
    }

    public static void use_case3() {
        System.out.println("\n#case 3: 命名柯里化（logger(level) 返回单参 logger）");
        Function<String, String> infoLogger = logger("INFO");
        Function<String, String> errorLogger = logger("ERROR");
        System.out.println(infoLogger.apply("服务启动"));
        System.out.println(errorLogger.apply("连接失败"));
    }

    // use_case4: decorate 命名柯里化 —— 直接呼应 Readme ILogDecorator.decorate 命名风格
    // decorate(level) 返回单参 Function<String,String>，level 决定前缀
    public static Function<String, String> decorate(String level) {
        return message -> "[" + level + "] " + message;
    }

    public static void use_case4() {
        System.out.println("\n#case 4: decorate 命名柯里化（呼应 Readme ILogDecorator.decorate）");
        Function<String, String> debug = decorate("DEBUG");
        Function<String, String> warn = decorate("WARN");
        System.out.println(debug.apply("调试信息"));
        System.out.println(warn.apply("告警信息"));
    }
}
