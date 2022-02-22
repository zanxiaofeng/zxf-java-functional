package zxf.java.functional.core.list;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.OptionalCurrying;
import zxf.java.functional.core.function.CheckedFunction;
import zxf.java.functional.core.optional.Optional;
import zxf.java.functional.core.optional.OptionalCases;

import java.util.ArrayList;
import java.util.function.Function;

public class ListCases {
    //Applicative--左结合，第一个参数用函子map，后面的参数用应用子apply
    public static void use_case3() throws Exception {
        List<Integer> list1 = new List<>(new ArrayList());
        List<Integer> list2 = new List<>(new ArrayList());

        Function<Integer, CheckedFunction<Integer, String>> curriedTriFunction = Currying.curryingFunction(OptionalCases::mulAndToString);
        List<CheckedFunction<Integer, String>> result1 = list1.map(curriedTriFunction);
        List<String> result = list2.applyChecked(result1);
    }

    public static String mulAndToString(Integer a, Integer b) {
        return String.valueOf(a * b);
    }
}
