package zxf.java.functional.pattern.designpattern;

import java.util.function.Function;

/**
 * 策略模式的函数式实现，对应 Readme「函数式设计模式」。
 * 策略对象 = Function/Predicate 实例，无需定义策略接口及多个实现类。
 */
public class StrategyCases {

    public static void main(String[] args) {
        use_case1();
    }

    // 价格计算：策略 = Function<Double, Double> 实例
    // 对比 OOP：OOP 需定义 DiscountStrategy 接口 + NoDiscount/TenOff/FullReduction 多个实现类
    public static double calcPrice(double original, Function<Double, Double> discountStrategy) {
        return discountStrategy.apply(original);
    }

    // 策略模式：策略对象 = Function 实例，并可链式组合（OOP 策略难以做到）
    public static void use_case1() {
        System.out.println("\n#case 1: 策略模式（策略 = Function 实例，可组合）");
        double original = 100.0;

        // 三种独立策略，均为 Function<Double, Double> 实例
        Function<Double, Double> noDiscount = p -> p;
        Function<Double, Double> tenPercentOff = p -> p * 0.9;
        Function<Double, Double> fullReduction = p -> p >= 100 ? p - 20 : p;

        System.out.println("原价 " + original + " => 无折扣:       " + calcPrice(original, noDiscount));
        System.out.println("原价 " + original + " => 九折:         " + calcPrice(original, tenPercentOff));
        System.out.println("原价 " + original + " => 满100减20:    " + calcPrice(original, fullReduction));

        // 策略组合：先九折再减10，体现函数式策略的可组合性（OOP 策略对象无法直接组合）
        Function<Double, Double> composed = tenPercentOff.andThen(p -> p - 10);
        System.out.println("原价 " + original + " => 九折后减10:    " + calcPrice(original, composed));
    }
}
