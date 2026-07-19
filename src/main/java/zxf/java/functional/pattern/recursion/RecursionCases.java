package zxf.java.functional.pattern.recursion;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

/**
 * 递归演示，对应 Readme「函数式的基本模式 - 递归」。
 * 演示普通递归、Stream 递推对比、尾递归思想（注释说明 Java 不直接优化尾调用）。
 */
public class RecursionCases {

    public static void main(String[] args) {
        use_case1();
        use_case2();
        use_case3();
    }

    // 普通递归：阶乘 factorial(n) = n * factorial(n-1)
    public static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    // 普通递归：斐波那契 fib(n) = fib(n-1) + fib(n-2)
    public static long fib(int n) {
        if (n < 2) return n;
        return fib(n - 1) + fib(n - 2);
    }

    // 普通递归（阶乘与斐波那契）：函数自调用，每一层等待下层结果后再计算
    public static void use_case1() {
        System.out.println("\n#case 1: 普通递归（阶乘/斐波那契）");
        System.out.println("factorial(5) = " + factorial(5));
        System.out.println("factorial(10) = " + factorial(10));
        System.out.println("fib(10) = " + fib(10));
        System.out.println("fib(20) = " + fib(20));
    }

    // 用 Stream.iterate 做递推，对比递归：把递归的"自顶向下"改写为"自底向上"的迭代生成
    public static void use_case2() {
        System.out.println("\n#case 2: 用 Stream.iterate 做递推（与递归对比）");
        // 自然数递推累乘 => 阶乘序列: f(0)=1, f(n)=f(n-1)*n
        List<BigInteger> factorials = Stream.iterate(new BigInteger[]{BigInteger.ONE, BigInteger.ZERO},
                        pair -> new BigInteger[]{pair[0].multiply(pair[1].add(BigInteger.ONE)), pair[1].add(BigInteger.ONE)})
                .limit(11)
                .map(pair -> pair[0])
                .toList();
        System.out.println("阶乘序列(0..10): " + factorials);

        // 斐波那契递推: (a, b) -> (b, a+b)
        List<Long> fibs = Stream.iterate(new long[]{0L, 1L}, pair -> new long[]{pair[1], pair[0] + pair[1]})
                .limit(15)
                .map(pair -> pair[0])
                .toList();
        System.out.println("斐波那契序列(前15项): " + fibs);
    }

    // 尾递归思想：把中间结果作为累加器参数向下传递，使递归调用成为最后一步操作。
    // 注意：Java 不直接优化尾调用（不会把尾递归转为循环），深度过大仍会 StackOverflow，
    // 因此生产环境推荐 use_case2 的 Stream.iterate 迭代写法，这里仅演示尾递归的等价改写思想。
    public static long factorialTail(int n, long acc) {
        if (n <= 1) return acc;
        return factorialTail(n - 1, acc * n);
    }

    public static long fibTail(int n, long a, long b) {
        if (n == 0) return a;
        return fibTail(n - 1, b, a + b);
    }

    // 尾递归思想（Java 不优化尾调用，仅作思想演示，生产应使用迭代/Stream）
    public static void use_case3() {
        System.out.println("\n#case 3: 尾递归思想（Java 不直接优化尾调用，深度大仍会 StackOverflow）");
        System.out.println("factorialTail(5, 1) = " + factorialTail(5, 1));
        System.out.println("factorialTail(20, 1) = " + factorialTail(20, 1));
        System.out.println("fibTail(10, 0, 1) = " + fibTail(10, 0, 1));
        System.out.println("fibTail(50, 0, 1) = " + fibTail(50, 0, 1) + " (普通递归为指数时间不可行；尾递归形式为线性时间，但 Java 不做尾调用优化，超大 n 仍需迭代/Stream)");
    }
}
