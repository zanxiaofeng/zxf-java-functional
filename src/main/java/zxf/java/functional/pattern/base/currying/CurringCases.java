package zxf.java.functional.pattern.base.currying;

import java.util.function.Function;

public class CurringCases {

    public void use_case1(){
        Function<Integer, Integer> add4 = Currying.curryingFunction(4, CurringCases::addBi);
        add4.apply(5);

        Function<Integer, Integer> add5 = Currying.curryingFunction(5, CurringCases::addBi);
        add4.apply(5);
    }

    public static Integer addBi(Integer x, Integer y){
        return x + y;
    }

    public static Integer addBi4(Integer y){
        return 4 + y;
    }

    public static Integer addBi5(Integer y){
        return 4 + y;
    }

    public static Integer addTri(Integer x, Integer y, Integer z){
        return x + y;
    }
}
