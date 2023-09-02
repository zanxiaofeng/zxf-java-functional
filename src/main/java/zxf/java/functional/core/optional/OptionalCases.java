package zxf.java.functional.core.optional;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.OptionalCurrying;
import zxf.java.functional.core.function.checked.CheckedFunction;

import java.util.function.Function;

public class OptionalCases {
    public static void main(String[] args) throws Exception {
        use_case1();
        use_case2();
        use_case3();
        use_case4();
    }

    //Functor
    public static void use_case1() {
        System.out.println("use_case1");
        Optional<Integer> optional999 = new Optional<>(999);
        Optional<String> optional999Add10 = optional999.map(OptionalCases::add10);
        System.out.println(optional999Add10.get());

        Optional<Integer> optional2 = new Optional<>();
        Optional<String> optional2add10 = optional2.map(OptionalCases::add10);
        System.out.println(optional2add10.get());
    }

    //Monad
    public static void use_case2() {
        System.out.println("use_case2");
        Optional<Integer> optional999 = new Optional<>(999);
        Optional<String> optional999Add100 = optional999.flatMap(OptionalCases::add100);
        System.out.println(optional999Add100.get());

        Optional<Integer> optionalNull = new Optional<>();
        Optional<String> optionalNullAdd100 = optionalNull.flatMap(OptionalCases::add100);
        System.out.println(optionalNullAdd100.get());
    }

    //Applicative--左结合，Val->Func->*Func by apply val*->Val, 第一个参数用函子map，后面的参数用应用子apply
    public static void use_case3() throws Exception {
        System.out.println("use_case3");
        Function<Integer, CheckedFunction<Integer, String>> curriedTriFunction = Currying.curryingFunction(OptionalCases::mulAndToString);

        Optional<Integer> optional10 = new Optional<>(10);
        Optional<Integer> optional999 = new Optional<>(999);
        Optional<String> result = optional999.applyChecked(optional10.map(curriedTriFunction));
        System.out.println(result.get());
    }

    //右结合, Func->*Func by apply val*->Val
    public static void use_case4() throws Exception {
        System.out.println("use_case4");
        OptionalCurrying.BiCurryingFunction<Integer, Integer, String> curriedTriFunction = OptionalCurrying.curryingFunction(OptionalCases::mulAndToString);

        Optional<Integer> optional10 = new Optional<>(10);
        Optional<Integer> optional999 = new Optional<>(999);
        Optional<String> result = curriedTriFunction.apply(optional10).apply(optional999);
        System.out.println(result.get());
    }

    public static String add10(Integer value) {
        return String.valueOf(value + 10);
    }

    public static Optional<String> add100(Integer value) {
        if (value == null) {
            return new Optional();
        }
        return new Optional<>(String.valueOf(value + 100));
    }

    public static String mulAndToString(Integer left, Integer right) {
        String result = String.valueOf(left * right);
        System.out.println(String.format("%d * %d = %s", left, right, result));
        return result;
    }
}
