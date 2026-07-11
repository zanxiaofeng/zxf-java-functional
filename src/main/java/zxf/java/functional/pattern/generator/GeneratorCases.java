package zxf.java.functional.pattern.generator;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 无限流生成器演示，对应 Readme「函数式的基本模式 - 生成器 / 无限流」。
 * 用 Supplier / Stream.generate / Stream.iterate 产生无限流，配合 limit 消费。
 * 生成器的本质 = 一个不断产出下一个值的状态转移函数。
 */
public class GeneratorCases {

    public static void main(String[] args) {
        use_case1();
        use_case2();
        use_case3();
    }

    // 自然数流：Stream.iterate(seed, nextUnaryOp) 生成无限递推流
    // iterate 形态：f(0)=seed, f(n+1)=unaryOp(f(n))
    public static void use_case1() {
        System.out.println("\n#case 1: 自然数流（Stream.iterate 递推生成器）");
        List<Integer> naturals = Stream.iterate(1, n -> n + 1)
                .limit(10)
                .toList();
        System.out.println("自然数前10项: " + naturals);

        // 平方数流：iterate 把状态转移函数作为生成规则
        List<Integer> squares = Stream.iterate(1, n -> n + 1)
                .limit(6)
                .map(n -> n * n)
                .toList();
        System.out.println("平方数前6项: " + squares);
    }

    // 斐波那契流：iterate 以 pair 状态推进，体现"生成器 = 状态转移函数"
    public static void use_case2() {
        System.out.println("\n#case 2: 斐波那契流（iterate 以 pair 为状态推进）");
        List<Long> fibs = Stream.iterate(new long[]{0L, 1L}, p -> new long[]{p[1], p[0] + p[1]})
                .limit(10)
                .map(p -> p[0])
                .toList();
        System.out.println("斐波那契前10项: " + fibs);
    }

    // 随机数流：Stream.generate(Supplier) 产生无限随机流
    // Supplier 是生成器的最小单元：每次调用 get() 产出下一个值
    public static void use_case3() {
        System.out.println("\n#case 3: 随机数流（Stream.generate + Supplier 生成器）");
        Random random = new Random(42); // 固定种子便于复现
        Supplier<Integer> dice = () -> random.nextInt(6) + 1;
        List<Integer> rolls = Stream.generate(dice).limit(10).toList();
        System.out.println("掷骰子10次: " + rolls);
        System.out.println("Supplier.get() 单次取值: " + dice.get() + ", " + dice.get());
    }
}
