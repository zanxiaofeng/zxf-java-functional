package zxf.java.functional.pattern.currying;

import zxf.java.functional.core.Currying;
import zxf.java.functional.core.Partial;
import zxf.java.functional.core.function.checked.CheckedFunction;


public class CurringCases {
    public static void main(String[] args) throws Exception {
        use_case1();
    }

    public static void use_case1() throws Exception {
        CheckedFunction<Integer, Integer> add4 = Partial.partialFunction(4, CurringCases::addBi);
        add4.apply(5);

        CheckedFunction<Integer, Integer> add5 = Partial.partialFunction(5, CurringCases::addBi);
        add4.apply(5);
    }

    public static Integer addBi(Integer x, Integer y) {
        return x + y;
    }

    public static Integer addBi4(Integer y) {
        return 4 + y;
    }

    public static Integer addBi5(Integer y) {
        return 4 + y;
    }

    public static Integer addTri(Integer x, Integer y, Integer z) {
        return x + y;
    }
}
