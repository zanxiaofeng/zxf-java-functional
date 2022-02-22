package zxf.java.functional.core.list;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.function.CheckedFunction;

import java.util.function.Function;

public class ListCases {
    public static void main(String[] args) throws Exception {
        use_case3();
    }

    //Applicative--左结合，第一个参数用函子map，后面的参数用应用子apply
    public static void use_case3() throws Exception {
        List<Integer> list1 = new List<>(23, 45, 67);
        List<Integer> list2 = new List<>(10, 100, 200);

        Function<Integer, CheckedFunction<Integer, String>> curriedTriFunction = Currying.curryingFunction(ListCases::mulAndToString);
        List<CheckedFunction<Integer, String>> result1 = list1.map(curriedTriFunction);
        List<String> result = list2.applyChecked(result1);
    }

    public static String mulAndToString(Integer a, Integer b) {
        System.out.println(String.format("%d * %d = %d", a, b, a * b));
        return String.valueOf(a * b);
    }
}
