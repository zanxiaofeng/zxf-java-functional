package zxf.java.functional.optional;

import zxf.java.functional.optional.model.Address;
import zxf.java.functional.optional.model.Customer;
import zxf.java.functional.optional.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Optional 教学演示，对应 Readme 「Optional」 章节 13 个方法。
 *
 * <p>覆盖：创建（of/ofNullable/empty）、取值（orElse/orElseGet/orElseThrow）、
 * 消费（ifPresent/ifPresentOrElse）、管道化（filter/or/map/flatMap）、转换为流（stream）、
 * 不推荐方法组（get/isPresent/isEmpty/orElseThrow 无参）。</p>
 */
public class OptionalCases {

    public static void main(String[] args) {
        use_case1();              // of / ofNullable / orElse
        use_case2();              // orElseThrow
        use_case3();              // map.map.map 完整闭环（对应 Readme Case 3）
        use_case3_empty();        // map.map.map 在中间值为 null 时走 else 分支
        use_case4_1();            // ifPresent
        use_case4_2();            // ifPresentOrElse
        use_case5_empty();        // empty() 与 ofNullable(null) 等价性
        use_case6_orElseGet();    // orElse vs orElseGet（eager 求值陷阱）
        use_case7_filter();       // filter
        use_case8_or();           // or（回退链）
        use_case9_flatMap();      // flatMap vs map（对应 Readme Monad）
        use_case10_stream();      // stream() 与 list.stream() 衔接
        use_case11_discouraged(); // 不推荐方法组: get/isPresent/isEmpty/orElseThrow()
    }

    // ===== 创建 + 取值 =====

    // of / ofNullable / orElse（对应 Readme Case 1: orElse）
    public static void use_case1() {
        System.out.println("use_case1 of / ofNullable / orElse");
        // 原始写法：手动 null 判断
        Integer int1 = getInteger();
        if (int1 == null) {
            int1 = 23;
        }
        System.out.println("  原始写法 (if null -> 23) = " + int1);

        // Optional 写法：of 构造非空，ofNullable 接受 null，orElse 提供默认值
        Integer int2 = getOptInteger().orElse(23);
        System.out.println("  Optional 写法 orElse(23) = " + int2);
        Integer int3 = getOptIntegerEmpty().orElse(23);
        System.out.println("  空 Optional orElse(23) = " + int3);
    }

    // orElseThrow（对应 Readme Case 2: orElseThrow）
    public static void use_case2() {
        System.out.println("use_case2 orElseThrow");
        // 原始写法
        Integer int1 = getInteger();
        if (int1 == null) {
            throw new RuntimeException("******");
        }
        System.out.println("  原始写法通过 = " + int1);

        // Optional 写法：空时抛出自定义异常（Supplier 惰性求值）
        Integer int2 = getOptInteger().orElseThrow(() -> new RuntimeException("******"));
        System.out.println("  非空 orElseThrow 通过 = " + int2);
        try {
            getOptIntegerEmpty().orElseThrow(() -> new RuntimeException("值为空"));
            System.out.println("  ERROR: 应当抛异常");
        } catch (RuntimeException e) {
            System.out.println("  空 orElseThrow 抛出: " + e.getMessage());
        }
    }

    // ===== map.map.map 完整闭环（对应 Readme Case 3） =====

    // map.map.map 链式闭环：以 ifPresentOrElse 消费最终结果
    public static void use_case3() {
        System.out.println("use_case3 map.map.map 完整闭环");
        // 原始写法：层层 if null 判断（深层嵌套）
        Order order = Order.getOrder();
        if (order != null) {
            Customer customer = order.getCustomer();
            if (customer != null) {
                Address address = customer.getAddress();
                if (address != null) {
                    System.out.println("  原始写法 city = " + address.getCity());
                }
            }
        }

        // Optional 写法：map.map.map + ifPresentOrElse，扁平化、无嵌套
        Optional.ofNullable(Order.getOrder())
                .map(Order::getCustomer)
                .map(Customer::getAddress)
                .map(Address::getCity)
                .ifPresentOrElse(
                        city -> System.out.println("  Optional 写法 city = " + city),
                        () -> System.out.println("  Optional 写法: 无 city"));
    }

    // 同一条 map.map.map 链，当中间值（customer）为 null 时自动回退到 empty 分支
    public static void use_case3_empty() {
        System.out.println("use_case3_empty map.map.map 在 customer 为 null 时走 else 分支");
        Order orderWithNullCustomer = new Order();
        orderWithNullCustomer.setId("o-2");
        orderWithNullCustomer.setAmount(0.0);
        orderWithNullCustomer.setCustomer(null); // customer 为 null

        Optional.ofNullable(orderWithNullCustomer)
                .map(Order::getCustomer)      // customer 为 null -> empty
                .map(Customer::getAddress)    // 链路短路，下游不再执行
                .map(Address::getCity)
                .ifPresentOrElse(
                        city -> System.out.println("  city = " + city),
                        () -> System.out.println("  走 else 分支: customer 为 null，无 city"));
    }

    // ===== 消费 =====

    // ifPresent（对应 Readme Case 4: ifPresent）
    public static void use_case4_1() {
        System.out.println("use_case4_1 ifPresent");
        // 原始写法：if (x != null) { doSomething(x); }
        Integer int1 = getInteger();
        if (int1 != null) {
            System.out.println("  原始写法处理: " + int1);
        }

        // Optional 写法：仅在有值时执行副作用，空时无操作
        getOptInteger().ifPresent(int2 -> System.out.println("  非空 ifPresent 处理: " + int2));
        getOptIntegerEmpty().ifPresent(int2 -> System.out.println("  这行不会打印"));
        System.out.println("  空 Optional 的 ifPresent 无输出（上面无打印即证明）");
    }

    // ifPresentOrElse：有值/空值分别处理
    public static void use_case4_2() {
        System.out.println("use_case4_2 ifPresentOrElse");
        // 原始写法：if/else
        Integer int1 = getInteger();
        if (int1 != null) {
            System.out.println("  原始写法有值: " + int1);
        } else {
            System.out.println("  原始写法空值");
        }

        // Optional 写法：一个表达式同时覆盖有值/空值两条分支
        getOptInteger().ifPresentOrElse(
                int2 -> System.out.println("  非空 ifPresentOrElse 有值: " + int2),
                () -> System.out.println("  非空 ifPresentOrElse 空值分支（不执行）"));
        getOptIntegerEmpty().ifPresentOrElse(
                int2 -> System.out.println("  空 ifPresentOrElse 有值分支（不执行）"),
                () -> System.out.println("  空 ifPresentOrElse 空值分支"));
    }

    // ===== empty 与 ofNullable(null) 等价性 =====

    // empty() 与 ofNullable(null) 语义完全等价
    public static void use_case5_empty() {
        System.out.println("use_case5 empty() 与 ofNullable(null) 等价性");
        Optional<String> fromEmpty = Optional.empty();
        Optional<String> fromNullable = Optional.ofNullable(null);
        System.out.println("  Optional.empty()           = " + fromEmpty);
        System.out.println("  Optional.ofNullable(null)  = " + fromNullable);
        System.out.println("  equals? " + fromEmpty.equals(fromNullable));
        System.out.println("  结论: 两者完全等价，empty() 语义更显式，ofNullable(null) 常用于未知来源值");
    }

    // ===== orElseGet 与 orElse 的 eager 求值陷阱（关键） =====

    // orElse 的参数【总是】被求值（eager）；orElseGet 的 Supplier 仅在空时才求值（lazy）
    public static void use_case6_orElseGet() {
        System.out.println("use_case6 orElse vs orElseGet（eager 求值陷阱）");
        Optional<String> present = Optional.of("real");

        System.out.println("  [场景] Optional 非空，调用 orElse(slowDefault()):");
        String a = present.orElse(slowDefault()); // slowDefault 仍会被调用！
        System.out.println("  结果 = " + a);

        System.out.println("  [场景] Optional 非空，调用 orElseGet(() -> slowDefault()):");
        String b = present.orElseGet(() -> slowDefault()); // slowDefault 不会被调用
        System.out.println("  结果 = " + b);

        System.out.println("  结论: orElse 实参总是先求值（eager），默认值有副作用/昂贵时务必用 orElseGet（lazy）");
    }

    // 带副作用的默认值生产方法（用于演示 eager 求值陷阱）
    private static String slowDefault() {
        System.out.println("  >> [slowDefault 被调用！产生默认值]");
        return "default-from-slowDefault";
    }

    // ===== filter =====

    // filter：谓词成立则保留原 Optional，否则变为 empty
    public static void use_case7_filter() {
        System.out.println("use_case7 filter（谓词过滤）");
        Optional<Integer> kept = Optional.of(10).filter(x -> x > 5);
        Optional<Integer> dropped = Optional.of(10).filter(x -> x > 100);
        System.out.println("  Optional.of(10).filter(x -> x > 5)   = " + kept + " (保留)");
        System.out.println("  Optional.of(10).filter(x -> x > 100) = " + dropped + " (过滤为 empty)");
        System.out.println("  保留值 = " + kept.orElse(-1) + ", 过滤后取默认 = " + dropped.orElse(-1));
    }

    // ===== or（回退链） =====

    // or：空时切换到另一个 Optional，可继续链式（对比 orElseGet 返回的是值，无法继续链）
    public static void use_case8_or() {
        System.out.println("use_case8 or（回退链）");
        Optional<Integer> result = Optional.<Integer>empty()
                .or(() -> Optional.empty())
                .or(() -> Optional.of(99));
        System.out.println("  empty().or(empty).or(of(99)) = " + result);
        System.out.println("  继续链式 filter/map: " + result.filter(x -> x > 50).map(x -> x * 2));
        System.out.println("  对比: orElseGet 返回值（Integer），无法继续 Optional 链; or 返回 Optional，可继续链式");
    }

    // ===== flatMap vs map（对应 Readme Monad） =====

    // 模拟一个返回 Optional<Customer> 的方法（如 Repository 查询）
    private static Optional<Customer> findCustomer(Order order) {
        return Optional.ofNullable(order).map(Order::getCustomer);
    }

    // map：函数返回普通值；flatMap：函数返回 Optional（Monad，避免 Optional<Optional<T>>）
    public static void use_case9_flatMap() {
        System.out.println("use_case9 flatMap vs map（对应 Readme Monad）");
        Order order = Order.getOrder();

        // map：getCustomer 返回普通 Customer，map 自动包成 Optional<Customer>
        Optional<Customer> byMap = Optional.ofNullable(order).map(Order::getCustomer);
        System.out.println("  map(Order::getCustomer) name = "
                + byMap.map(Customer::getName).orElse("(空)"));

        // flatMap：findCustomer 返回 Optional<Customer>，flatMap 拍平嵌套
        Optional<Customer> byFlatMap = Optional.ofNullable(order).flatMap(OptionalCases::findCustomer);
        System.out.println("  flatMap(this::findCustomer) name = "
                + byFlatMap.map(Customer::getName).orElse("(空)"));

        // 反例：对返回 Optional 的方法误用 map，会得到 Optional<Optional<Customer>>
        Optional<Optional<Customer>> nested = Optional.ofNullable(order).map(OptionalCases::findCustomer);
        System.out.println("  误用 map 得到嵌套 Optional = " + nested);
        System.out.println("  结论: 函数返回普通值用 map，返回 Optional 用 flatMap（Monad 核心模式）");
    }

    // ===== stream() 与 list.stream() 衔接 =====

    // stream()：把 Optional 转为 0 或 1 个元素的 Stream，无缝衔接集合流
    public static void use_case10_stream() {
        System.out.println("use_case10 stream()（与 list.stream() 衔接）");
        Optional<String> present = Optional.of("hello");
        Optional<String> absent = Optional.empty();

        // 非空 Optional.stream() 产出 1 个元素，空 Optional.stream() 产出 0 个元素
        long countFromPresent = present.stream().count();
        long countFromAbsent = absent.stream().count();
        System.out.println("  非空 Optional.stream().count() = " + countFromPresent);
        System.out.println("  空   Optional.stream().count() = " + countFromAbsent);

        // 典型场景：flatMap 把多个 Optional 串进同一条流
        List<Optional<String>> optionals = List.of(
                Optional.of("a"), Optional.empty(), Optional.of("b"), Optional.empty(), Optional.of("c"));
        List<String> presentValues = optionals.stream()
                .flatMap(Optional::stream) // 空 Optional 贡献 0 元素，非空贡献 1 元素
                .toList();
        System.out.println("  List<Optional> 过滤出非空值 = " + presentValues);
    }

    // ===== 不推荐方法组（对应 Readme 不推荐使用） =====

    // get / isPresent / isEmpty / orElseThrow() 无参：不推荐，附替代方案。
    // 注意：真正的反模式是「isPresent()/isEmpty() + get() 替代 map/ifPresent 链式调用」这一组合用法，
    // 而非这些方法本身——例如 orElseThrow() 场景用 isPresent() 做防御性判空完全合理。
    public static void use_case11_discouraged() {
        System.out.println("use_case11 不推荐方法组: get / isPresent / isEmpty / orElseThrow()");

        // get()：空时抛 NoSuchElementException，暴露内部状态。替代: orElse/orElseGet/ifPresent
        Optional<String> opt = Optional.of("x");
        System.out.println("  get() = " + opt.get()
                + "  [不推荐: 空时抛 NoSuchElementException，替代 orElse/orElseGet]");

        // isPresent()：本质退化回 if 语句，丧失 Optional 声明式优势。替代: ifPresent/ifPresentOrElse
        System.out.println("  isPresent() = " + opt.isPresent()
                + "  [不推荐: 退化回 if 判断，替代 ifPresent]");

        // isEmpty()：与 isPresent 同理。替代: ifPresentOrElse 的空值分支
        System.out.println("  isEmpty() = " + opt.isEmpty()
                + "  [不推荐: 同 isPresent，替代 ifPresentOrElse]");

        // orElseThrow() 无参：抛出无语义的 NoSuchElementException。替代: orElseThrow(supplier) 带业务异常
        try {
            Optional.<String>empty().orElseThrow();
            System.out.println("  ERROR: 应当抛异常");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("  orElseThrow() 无参抛出: " + e.getClass().getSimpleName()
                    + "  [不推荐: 无业务语义，替代 orElseThrow(() -> new BusinessException(\"...\"))]");
        }
    }

    // ===== 辅助工厂方法 =====

    private static Integer getInteger() {
        return 232;
    }

    private static Optional<Integer> getOptInteger() {
        return Optional.of(232);
    }

    private static Optional<Integer> getOptIntegerEmpty() {
        return Optional.empty();
    }
}
