package zxf.java.functional.pattern.closure.cache;

import zxf.java.functional.core.Caching;

import java.util.function.Function;

/**
 * 闭包缓存 vs OOP 缓存对照演示，对应 Readme「闭包」章节。
 * 闭包：Caching.cachedFunction 把缓存封装在返回的 Function 闭包内部，外部完全无法访问状态；
 * OOP：OOPCache 用实例字段 cache 维护状态，状态与对象生命周期绑定、可被同类方法共享。
 * 两种方式行为等价，但闭包实现了状态封装，是更纯粹的函数式风格。
 */
public class OOPCacheCases {

    public static void main(String[] args) {
        use_case1();
    }

    // use_case1: 闭包缓存 vs OOP 缓存 并列输出对比（同样计算 x+2，对比缓存机制）
    public static void use_case1() {
        System.out.println("\n#case 1: 闭包缓存 vs OOP 缓存（同样计算 x+2，对比缓存机制）");

        // OOP 方式：缓存是对象的实例字段（OOPCache.cache），随对象存亡
        System.out.println("--- OOP 方式（缓存为对象字段）---");
        OOPCache oopCache = new OOPCache();
        System.out.println("[OOP] cachedCalculate(5) = " + oopCache.cachedCalculate(5));
        System.out.println("[OOP] cachedCalculate(5) = " + oopCache.cachedCalculate(5) + "（第二次命中缓存，无 realCalculate 打印）");
        System.out.println("[OOP] cachedCalculate(6) = " + oopCache.cachedCalculate(6));

        // 闭包方式：缓存被封装在返回的 Function 闭包内部，外部无法直接访问
        System.out.println("--- 闭包方式（缓存封装在 Function 闭包内）---");
        Function<Integer, Integer> closureCache = Caching.cachedFunction(x -> {
            System.out.println("realCalculate(" + x + ")");
            return x + 2;
        });
        System.out.println("[闭包] apply(5) = " + closureCache.apply(5));
        System.out.println("[闭包] apply(5) = " + closureCache.apply(5) + "（第二次命中缓存，无 realCalculate 打印）");
        System.out.println("[闭包] apply(6) = " + closureCache.apply(6));

        System.out.println("--- 结论：闭包把状态隐藏在函数内（外部完全不可达），OOP 把状态绑定在对象实例上 ---");
    }
}
