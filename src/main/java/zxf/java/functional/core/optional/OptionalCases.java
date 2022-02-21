package zxf.java.functional.core.optional;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.OptionalCurrying;
import zxf.java.functional.core.function.CheckedFunction;

import java.util.function.Function;

public class OptionalCases {
    public static void main(String[] args) throws Exception {
        use_case1();
        use_case2();
        use_case3();
    }

    //Functor
    public static void use_case1() {
        Optional<Integer> optional1 = new Optional<>(999);
        Optional<String> optional1_new = optional1.map(OptionalCases::toString);

        Optional<Integer> optional2 = new Optional<>();
        Optional<String> optional2_new = optional2.map(OptionalCases::toString);
    }

    //Monad
    public static void use_case2() {
        Optional<Integer> optional1 = new Optional<>(999);
        Optional<String> optional1_new = optional1.flatMap(OptionalCases::toOptString);

        Optional<Integer> optional2 = new Optional<>();
        Optional<String> optional2_new = optional2.flatMap(OptionalCases::toOptString);
    }

    //Applicative--left
    public static void use_case3() throws Exception {
        Optional<Integer> optional1 = new Optional<>(999);
        Optional<Integer> optional2 = new Optional<>(10);

        Function<Integer, CheckedFunction<Integer, String>> curriedTriFunction = Currying.curryingFunction(OptionalCases::mulAndToString);
        Optional<String> result = optional2.applyChecked(optional1.apply(new Optional<>(curriedTriFunction)));
    }

    //Applicative--right
    public static void use_case4() throws Exception {
        Optional<Integer> optional1 = new Optional<>(999);
        Optional<Integer> optional2 = new Optional<>(10);

        OptionalCurrying.BiCurryingFunction<Integer, Integer, String> curriedTriFunction = OptionalCurrying.curryingFunction(OptionalCases::mulAndToString);
        Optional<String> result = curriedTriFunction.apply(optional1).apply(optional2);
    }

    public static String toString(Integer value) {
        return value.toString();
    }

    public static Optional<String> toOptString(Integer value) {
        if (value <= 0) {
            return new Optional();
        }
        return new Optional<>(value.toString());
    }

    public static String mulAndToString(Integer a, Integer b) {
        return String.valueOf(a * b);
    }
}
