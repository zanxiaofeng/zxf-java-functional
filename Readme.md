# 编程范式
>- 过程式编程
>- 面向对象编程
>>* 支持过程式(纯静态方法类或纯数据类)
>- 函数式编程
>>* 支持过程式(不使用高阶函数)

# 函数式编程
### 函数
>- 函数作为类型
>- 函数作为函数入参（高阶函数，函数式多态）
>- 函数作为函数返回值（高阶函数）
### 不变量
### 元组

# Java８引入的函数式
>- Functional Interface
>- Lambda
>- Optional
>- Stream

# FunctionalInterface
>| Interface              | Parameter      | Return     |      Method      |         Description             |　　　      　Others    　 　　　   |
>|------------------------|----------------|------------|------------------|---------------------------------|---------------------------------|
>|  Supplier<T>           |   N/A          |   T        |      get         | 提供一个Ｔ类型的值　　　　　　　      |            N/A                  |
>|  Consumer<T>           |   T            |   void     |      accept      | 处理一个Ｔ类型的值　　　　　　　      |   andThen                       |
>|  BiConsumer<T,U>       |   T,U          |   void     |      accept      | 处理Ｔ和Ｕ类型的值　　　　　　　      |   andThen                       |
>|  Function<T,R>         |   T            |   R        |      apply       | 有一个Ｔ类型参数的函数　　　　　      |   compose, andThen, identity    |
>|  BiFunction<T,U,R>     |   T,U          |   R        |      apply       | 有一个Ｔ类型和一个U类型参数的函数     |   andThen                       |
>|  UnaryOperator<T>      |   T            |   T        |      apply       | 类型T上的一元操作                  |   compose, andThen, identity    |
>|  BinaryOperator<T>     |   T,T          |   T        |      apply       | 类型T上的二元操作                  |   andThen, maxBy, minBy         |
>|  Predicate<T>          |   T            |   Boolean  |      test        | 单个参数Ｔ上的布尔值函　　　　　　　	 |   and, or, nagate, isEqual, not |
>|  BiPredicate<T,U>      |   T,U          |   Boolean  |      test        | 有两个参数T和Ｕ的布尔值函数　　　　　　|   and, or, nagate               |

# Lambda
>- 标准写法
`(product)->{
	return product.getId();
}`
>- 单行写法
`(product)->product.getId()`
>- 方法引用
>>- object:instanceMethod
>>- Class::instanceMethod
>>- Class::staticMethod
>- 构造器引用
>>- Class::new


# Optional
### Optional 基础
>- 创建
>>- `public static <T> Optional<T> of(T value)`
>>- `public static <T> Optional<T> ofNullable(T value)`
>>- `public static <T> Optional<T> empty()`
>- 取值
>>- `public T orElse(T other)`
>>- `public T orElseGet(Supplier<? extends T> supplier)`
>>- `public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)`
>- 消费
>>- `public void ifPresent(Consumer<? super T> action)`
>>- `public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)`
>- 管道化处理
>>- `public Optional<T> filter(Predicate<? super T> predicate)`
>>- `public Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)`
>>- `public <U> Optional<U> map(Function<? super T, ? extends U> mapper)`
>>- `public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)`
>- 转换为流
>>- `public Stream<T> stream()`
>- 不推荐使用
>>- `public T orElseThrow()`
>>- `public T get()`
>>- `public boolean isPresent()`
>>- `public boolean isEmpty()`
### Optional 使用场景
>- Case 1: orElse
>- Case 2: orElseThrow
>- Case 3: map.map.map
>- Case 4: ifPresent

# Stream
### Stream 基础
### Stream Collect
### Stream Reduce
### Stream Parallel
### Stream 基本类型流

# 三大范式中数据与函数关系
>- 过程式中，数据作为过程入参，数据与函数分离 
>- OOP中，函数作为数据类的方法，可以和数据定义在一起，数据拥有函数
>- 函数式闭包中，闭包数据包含在函数内部，函数拥有私有数据


# 函数式的基本模式
### 高阶函数-HOF
### 闭包-Closure
>- 闭包是只有一个方法的匿名类的一个实例，闭包引用的局部变量将Ｃopy到匿名类的成员变量中
>- 依据闭包对其引用的变量的作用以及读写方式，可以将其分为参数配置型（只读，以参数及配置类型而定）变量引用、底层数据型（只读，多为集合型）变量引用以及数据缓存型（读写，多为Ｍａｐ）引用
### 柯里化-Currying
### 组合-Compose
### 生成器
### 递归

# 设计模式的函数式实现
### 模板方法
### 策略模式
### 装饰器