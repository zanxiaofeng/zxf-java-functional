package zxf.java.functional.core.list;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.function.CheckedFunction;

import java.util.function.Function;

public class ListCases {
    public static void main(String[] args) throws Exception {
        use_case1();
        use_case2();
        use_case3();
    }

    //Functor
    public static void use_case1() {
        System.out.println("use_case1");
        List<Integer> numbers = new List<>(23, 45, 67);
        List<String> strings = numbers.map(ListCases::add10);
    }

    //Monad
    public static void use_case2() {
        System.out.println("use_case2");
        List<Integer> numbers = new List<>(23, 45, 67);
        List<String> strings = numbers.flatMap(ListCases::add100);
    }

    //Applicative--左结合，第一个参数用函子map，后面的参数用应用子apply
    public static void use_case3() throws Exception {
        System.out.println("use_case3");
        Function<Integer, CheckedFunction<Integer, String>> curriedTriFunction = Currying.curryingFunction(ListCases::mulAndToString);

        List<Integer> numbersLeft = new List<>(23, 45, 67);
        List<CheckedFunction<Integer, String>> middleResult = numbersLeft.map(curriedTriFunction);
        List<Integer> numbersRight = new List<>(10, 100, 200);
        List<String> finalResult = numbersRight.applyChecked(middleResult);
    }

    private static String add10(Integer value) {
        String result = String.valueOf(value + 10);
        System.out.println(String.format("%d + 10 = %s", value, result));
        return result;
    }

    private static List<String> add100(Integer value) {
        String result = String.valueOf(value + 100);
        System.out.println(String.format("%d, %s", value, result));
        return new List<>(value.toString(), result);
    }

    private static String mulAndToString(Integer left, Integer right) {
        String result = String.valueOf(left * right);
        System.out.println(String.format("%d * %d = %s", left, right, result));
        return result;
    }
}
