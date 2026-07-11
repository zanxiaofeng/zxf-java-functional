# 编程范式
>- 过程式编程
>- 面向对象编程
>>* 支持过程式(纯静态方法类或纯数据类)
>- 函数式编程
>>* 支持过程式(不使用高阶函数)

# 计算与数据的关系
>- 过程式（全局，输入, 局部, 输出）
>- 面向对象（全局，对象，输入, 局部，输出）
>- 函数式（输入，局部，输出）

# 数据
>- 基本类型，如 Int，Long，String
>- 复合类型，如 Structure，Class
>- 容器类型，如 Array，List，Map

# 函数式编程
## 函数
>- 函数作为类型和值
>- 函数作为类型：变量类型，参数类型（高阶函数），返回值类型（高阶函数）
>- 函数作为值：变量值，参数值（实参），返回值
## 不变量
## 元组

# 函数式多态（Java）
>- 函数作为入参的多态
>- 函数作为返回值的多态（不同情况返回不同函数）
>- 函数作为类成员变量形成新的面向对象与函数式结合的新型多态（以组合而非继承的方式实现的面向对象多态）

# Java 8 引入的函数式
>- Functional Interface
>- Lambda
>- Optional
>- Stream

# FunctionalInterface
>| Interface              | Parameter      | Return     |      Method      |         Description             |　　　      　Others    　 　　　   |
>|------------------------|----------------|------------|------------------|---------------------------------|---------------------------------|
>|  Supplier<T>           |   N/A          |   T        |      get         | 提供一个 T 类型的值　　　　　　　      |            N/A                  |
>|  Consumer<T>           |   T            |   void     |      accept      | 处理一个 T 类型的值　　　　　　　      |   andThen                       |
>|  BiConsumer<T,U>       |   T,U          |   void     |      accept      | 处理 T 和 U 类型的值　　　　　　　      |   andThen                       |
>|  Function<T,R>         |   T            |   R        |      apply       | 有一个 T 类型参数的函数　　　　　      |   compose, andThen, identity    |
>|  BiFunction<T,U,R>     |   T,U          |   R        |      apply       | 有一个 T 类型和一个 U 类型参数的函数     |   andThen                       |
>|  UnaryOperator<T>      |   T            |   T        |      apply       | 类型 T 上的一元操作                  |   compose, andThen, identity    |
>|  BinaryOperator<T>     |   T,T          |   T        |      apply       | 类型 T 上的二元操作                  |   andThen, maxBy, minBy         |
>|  Predicate<T>          |   T            |   boolean  |      test        | 单个参数 T 上的布尔值函数　　　　　　　   |   and, or, negate, isEqual, not |
>|  BiPredicate<T,U>      |   T,U          |   boolean  |      test        | 有两个参数 T 和 U 的布尔值函数　　　　　　|   and, or, negate               |

> 见 `function/FunctionalInterfaceCases.java`（9 个接口全覆盖，含默认/静态方法演示）。

# Java 中函数值的形式
## 方法引用
>- Class::instanceMethod（类实例方法引用）
>- Class::staticMethod（类静态方法引用）
>- Class::new（类构造器引用）
>- object::instanceMethod（对象实例方法引用）

> 见 `function/MethodReferenceCases.java`（四种形式并列对比，含等价 Lambda）。

## Lambda（立即函数）
>- 标准写法
```java
(product) -> {
    return product.getId();
}
```
>- 单行写法
```java
(product) -> product.getId()
```

> 见 `function/LambdaUsage.java`（标准写法 / 单行写法 / 无参 / 多语句块对比）。

# Java 函数式的特别
>- 函数作为类的成员变量可以实现面向对象和函数式结合的新多态模式（组合而非继承式多态）
>- 只有一个方法的接口（不包含 Default 方法）与函数可以相互转换，不仅便捷，而且命名接口使得函数式的使用在设计上更有意义（比如结合设计模式）
>- 一个函数值是否可以赋值给一个函数类型，首先要看函数值的参数类型、参数顺序、返回值类型是否与函数类型声明的形式兼容一致，其次要看函数值声明的 Exceptions(Throws 子句)能否被函数类型声明的 Exceptions(Throws 子句)覆盖包含；立即函数 Lambda 虽然没有显式的 Throws 子句，但编译器可以依据其代码中调用的函数的签名汇总出其可能 Throw 的 Exceptions，因此也要符合这个约束条件
>- https://stackoverflow.com/questions/18198176/java-8-lambda-function-that-throws-exceptionss

> 见 `function/FunctionalUsage.java`（函数作为类型/值、函数式多态三种形式、checked 异常 wrap 方案）。
> 抛 checked 异常的函数如何接入 JDK 标准 API，见 `core/function/throwing/ThrowingCases.java`。

# Optional
## Optional 基础
### 创建
>- `public static <T> Optional<T> of(T value)`（value 为 null 时抛 NPE，需确定非空才用）
>- `public static <T> Optional<T> ofNullable(T value)`（接受 null，更常用）
>- `public static <T> Optional<T> empty()`
### 取值
>- `public T orElse(T other)`
>- `public T orElseGet(Supplier<? extends T> supplier)`（延迟求值，与 orElse 的 eager 求值是该用哪个的关键）
>- `public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)`
### 消费
>- `public void ifPresent(Consumer<? super T> action)`
>- `public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)`
### 管道化处理
>- `public Optional<T> filter(Predicate<? super T> predicate)`
>- `public Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)`
>- `public <U> Optional<U> map(Function<? super T, ? extends U> mapper)`
>- `public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)`
### 转换为流
>- `public Stream<T> stream()`
### 不推荐使用
>- `public T orElseThrow()`
>- `public T get()`
>- `public boolean isPresent()`
>- `public boolean isEmpty()`

> 真正的反模式不是这几个方法本身，而是「用 `isPresent()` + `get()` 替代 `map` / `ifPresent`」——那等于把命令式 null 检查套了层 Optional 外壳，丢失了声明式的链式优势。
## Optional 使用场景
>- Case 1: orElse
>- Case 2: orElseThrow
>- Case 3: map.map.map
>- Case 4: ifPresent

> 见 `optional/OptionalCases.java`（13 个 API 全覆盖，含 orElse vs orElseGet 的 eager/lazy 陷阱、map.map.map 完整闭环）；
> JSON 序列化场景见 `optional/json/OptionalJsonCases.java`；模拟 Spring Data JPA `findById` 见 `optional/jpa/JpaCases.java`。

# Stream
## Stream 基础
### 创建
`Stream.of` / `Arrays.stream` / `Collection.stream()` / `Stream.generate` / `Stream.iterate` / `Stream.empty` / `Stream.concat` / `Stream.builder` / `Pattern.splitAsStream` / `Files.lines`
### 中间操作
`filter` / `map` / `flatMap` / `sorted` / `distinct` / `peek` / `limit` / `skip` / `takeWhile` / `dropWhile` / `mapToInt`
### 终止操作
`forEach` / `count` / `collect` / `reduce` / `min` / `max` / `findFirst` / `findAny` / `allMatch` / `anyMatch` / `noneMatch` / `toArray`

> 见 `stream/StreamCreationCases.java`（创建）、`stream/StreamIntermediateCases.java`（中间操作 + flatMap）、`stream/StreamUsageCases.java`（综合词频统计）。

## Stream Collect
`toList` / `toSet` / `toMap` / `joining` / `groupingBy`（单参 + 双参带下游）/ `partitioningBy` / `counting` / `summarizingInt` / `mapping`

> 见 `stream/StreamCollectCases.java`（Collectors 全家桶，直接用 Account 数据演示分组/分区/统计）。

## Stream Reduce
三种重载：
>- `Optional<T> reduce(BinaryOperator<T>)`
>- `T reduce(T identity, BinaryOperator<T>)`
>- `<U> U reduce(U identity, BiFunction<U,? super T,U>, BinaryOperator<U>)`

> 见 `stream/StreamReduceCases.java`（三种重载分别求和/求最大值/复合归约）。

## Stream Parallel
`Collection.parallelStream()` / `Stream.parallel()` / `sequential()` / `forEachOrdered`。注意并行流的相遇顺序与共享可变状态陷阱（不要在 forEach 里改非线程安全容器，应改用 collect）。

> 见 `stream/StreamParallelCases.java`（含共享状态丢失演示与正确做法）。

## Stream 基本类型流
`IntStream` / `LongStream` / `DoubleStream`，及 `range` / `rangeClosed` / `summaryStatistics` / `average` / `sum` / `max`。

> 见 `stream/IntegerStreamCases.java`（基本类型流与 summaryStatistics 教材级 API）。

# Clean Code（Stream, Optional）
>- 合理的换行
>- 舍得封装函数
>- 合理封装流式代码
>- 合理使用 Optional（map.map.map）
>- 返回 Stream 或 List？（设计）
>- 少用或者不用并行流

# 三大范式中数据与函数关系
>- 过程式中，数据作为过程入参，数据与函数分离
>- OOP 中，函数作为数据类的方法，可以和数据定义在一起，数据拥有函数
>- 函数式中，通常数据作为函数入参，数据与函数分离，数据只在调用时才与函数结合；函数式闭包可以在创建函数时就将数据与函数绑定，函数拥有数据

# 函数式的基本模式
## 高阶函数-HOF
>- 函数作入参 / 作返回值 / 作类成员变量（对应函数式多态三种形式）

> 见 `pattern/hof/HOFCases.java`。

## 闭包-Closure
>- 闭包是只有一个方法的匿名类的一个实例，闭包引用的局部变量将 Copy 到匿名类的成员变量中
>- 依据闭包对其引用的变量的作用以及读写方式，可以将其分为参数配置型（只读，以参数及配置类型而定）变量引用、底层数据型（只读，多为集合型）变量引用以及数据缓存型（读写，多为 Map）引用
>- 闭包在实现上是一个结构体或类，它存储了一个函数（通常是其入口地址）和一个关联的环境（相当于一个符号查找表），环境里是若干对符号和值的对应关系，它既要包括约束变量（该函数内部绑定的符号），也要包括自由变量（在函数外部定义但在函数内被引用），有些函数也可能没有自由变量。闭包和函数最大的不同在于，当捕捉闭包的时候，它的自由变量会在捕捉时被确定，这样即便脱离了捕捉时的上下文，它也能照常运行；捕捉时对于值的处理可以是值拷贝，也可以是名称引用，这通常由语言设计者决定，也可能由用户自行指定（如 C++）[维基百科]
>- Wrap Object

> 见 `pattern/closure/`（overview 三范式对照 / account 参数配置型 / cache 数据缓存型 vs OOP / store + Wrap Object）。

## 柯里化-Currying/Partial
>- 柯里化生成的就是闭包函数，其中被柯里化的参数就是闭包变量
>- 显式命名柯里化：出于设计考虑采用命名柯里化函数，可以提供设计的可理解性（如 `ILogDecorator.decorate` 方法）
>- 柯里化工具类：出于重用考虑使用通用柯里化工具类，可以降低代码重复，提供高一致性

> 见 `pattern/currying/CurryingCases.java`（Partial 偏应用、`core.Currying` 工具类、命名柯里化 logger/decorate）；
> 工具类实现见 `core/Currying.java`、`core/Partial.java`、`core/OptionalCurrying.java`。

## 组合-Compose
>- 组合（静态）是指通过函数定义用代码将一个或多个函数（HardCode）调用编织形成新的函数，一个定义代表一个新的组合。
>- 组合（半动态）是指通过函数定义用代码将一个或多个函数（至少一个作为入参，其他 HardCode）调用编织形成新的函数，一次调用创建一个新的组合并调用。
>- 组合（动态）是指定义一个组合函数，在其中通过代码将一个或多个函数（至少一个作为入参，其他 HardCode）编织形成新的函数（返回值）并返回，一次调用创建一个新的组合。
>- 静态——HardCode 函数，通过定义完成组合，通过执行完成调用；半动态——函数作为入参，通过执行完成组合并调用；动态——函数作为入参以及返回值，通过执行完成组合并返回组合函数，在返回的组合函数上执行完成调用。

> 见 `pattern/compose/ComposeCases.java`（静态 / 半动态 / 动态三种组合）。

## 生成器
用 `Supplier` / `Stream.generate` / `Stream.iterate` 构造无限流（如斐波那契流、自然数流），配合 `limit` 消费——把「递归的自顶向下」改为「迭代生成的自底向上」。

> 见 `pattern/generator/GeneratorCases.java`。

## 递归
>- 普通递归：函数自调用，每一层等待下层结果（阶乘、斐波那契）
>- 用 `Stream.iterate` 做递推，与递归对比
>- 尾递归思想：Java 不直接优化尾调用，但可写出尾递归形式以便理解（注释说明）

> 见 `pattern/recursion/RecursionCases.java`。

# 设计模式的函数式实现
## 模板方法
用 `Function` / `Consumer` 组合替代抽象类继承：骨架固定，可变步骤作为函数式钩子（参数）传入。

> 见 `pattern/designpattern/TemplateMethodCases.java`。

## 策略模式
策略对象 = `Function` / `Predicate` 实例，对比 OOP 策略接口（如不同折扣策略）。

> 见 `pattern/designpattern/StrategyCases.java`。

## 装饰器
用 `Function.andThen` / `compose` 或 `UnaryOperator<Function>` 包装实现装饰器（日志装饰、缓存装饰），呼应命名柯里化 `ILogDecorator.decorate`。

> 见 `pattern/designpattern/DecoratorCases.java`。

# 函数式数据（容器）处理模式
## 包装类型（泛型容器型数据）
>- 泛型集合类型，如 List[T]
>- 泛型不相交联合体，如 Optional<T>(Some[T], None)，Result<T,Err>(Success<T>, Failure<Err>)
## 问题：函数式如何将各类函数应用于此包装类型
>- 问题一（普通函数）：如何将普通函数应用于包装类型，这就是函子 Functor，如 Optional.map 方法；如将 `String toString(Integer value)` 应用于 `Optional<Integer>`。
>- 问题二（返回包装类型的普通函数）：如何将返回包装类型的普通函数应用于包装类型，这就是单子 Monad，如 Optional.flatMap 方法；如将 `Optional<Integer> toInt(String value)` 应用于 `Optional<String>`。
>- 问题三（函数包装类型）：如何将函数包装类型中包装的函数应用于另一包装类型，这就是应用子 Applicative，方法名通常是 apply；比如 `List<Val> * Optional<Func>` 或 `Optional<Val> * List<Func>`。
>- 问题四（如何创建函数包装类型）：可以直接用函数构造一个函数包装类型，也可以通过函子操作将一个返回函数的函数应用于普通包装类型，从而将其转换成函数包装类型；返回函数的函数可以自定义也可以是 Curry 或 Partial。
## 泛型容器类型的使用模式
>- 像 `Stream<T>`、`Optional<T>`、`Flux<T>`、`Mono<T>` 这类函数式泛型容器类型，其使用模式基本是：1. 容器类型实例的创建——从其他类型 T 的实例或 Supplier<T> 实例化出容器类型；2. 利用函数式操作对 T 进行各种过滤转换，使被包装类型由 T...R（可以进行多次转换）（Predicate<M1>，Function<M1,M2>）；3. 终止操作——消费 R 类型数据（Consumer<R>）

# core：从零手写的函数式原语
为揭示原理，本项目 `core` 包从零手写了函数式核心类型（非调用 JDK）：
>- `core/optional/Optional` + `core/list/List` + `core/stream/Stream`：手写 map / flatMap / apply，分别落地 **Functor / Monad / Applicative** 三态；自实现 Stream 用 Processor 链展现 pull-model 惰性求值。见 `core/optional/OptionalCases.java`、`core/list/ListCases.java`、`core/stream/Test.java`。
>- `core/function/Tri*`：JDK 没有 TriFunction/TriConsumer/TriPredicate，这里补齐三元函数式接口。
>- `core/function/checked` vs `core/function/throwing`：前者声明 `throws Exception`（显式传播，不能直接塞进 `Stream.map`）；后者把 checked 异常适配为 unchecked（可接入标准 API）。见 `core/function/throwing/ThrowingCases.java`。
>- `core/Currying` / `core/Partial` / `core/Caching`：通用柯里化、偏应用、闭包缓存（memoization）工具类。

# 为啥函数式编程是声明型编程
>- 不像命令式编程，函数式编程最基本的单位是函数，粒度比语句要大而功能又比较专一，所以以函数组合为最基础模式的函数式编程从代码角度看来必然是函数 level 的组合以及链式调用；另外基于函数式的框架通过将复杂计算中的控制流与计算抽离，从而使得可以通过不同函数的组合（控制流由核心框架固化）实现不同的具体功能，从而提供比命令式编程更具灵活性和代码重用的编程方法。从代码方面来看，函数式代码可以看做是函数 level 的组合调用，不涉及语句和控制流层面的代码，可以看作是函数组合调用的声明。
