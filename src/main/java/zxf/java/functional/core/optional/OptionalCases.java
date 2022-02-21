package zxf.java.functional.core.optional;

import zxf.java.functional.core.Currying;
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

    //Applicative
    public static void use_case3() throws Exception {
        Optional<Integer> optional1 = new Optional<>(999);
        Optional<Integer> optional2 = new Optional<>(10);

        Currying.BiCurryingFunction<Integer, Integer, String> curriedTriFunction= Currying.Common.curryingFunction(OptionalCases::mulAndToString);
        Optional<String> result = optional2.applyChecked(curriedTriFunction.apply(optional1));
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
