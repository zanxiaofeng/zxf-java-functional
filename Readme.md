# 编程范式
>- 过程式编程
>- 面向对象编程
>>* 支持过程式(纯静态方法类或纯数据类)
>- 函数式编程
>>* 支持过程式(不使用高阶函数)

# 函数式编程
### 函数
>- 函数作为类型和值
>- 函数作为类型：变量类型，参数类型（高阶函数），返回值类型（高阶函数）
>- 函数作为值：变量值，参数值（实参），返回值
### 不变量
### 元组


# 函数式多态（Ｊava）
>- 函数作为入参的多态
>- 函数作为返回值的多态（不同情况返回不同函数）
>- 函数作为类成员变量形成新的面向对象与函数式结合的新型多态（以组合而非继承的方式实现的面向对象多态）


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


# Java中函数值的形式
>- 方法引用
>>- Class::instanceMethod(类实例方法引用)
>>- Class::staticMethod(类静态方法引用)
>>- Class::new(类构造器引用)
>>- object:instanceMethod(对象实例方法引用)
>- 立即函数 - Lambda
>>- 标准写法
`(product)->{
	return product.getId();
}`
>>- 单行写法
`(product)->product.getId()`

# Java函数式的特别
>- 函数作为类的成用变量可以实现面向对象和函数式结合的新多态模式（组合而非继承式多态）
>- 只有一个方法的接口（不包含Ｄefault方法）与函数可以相互转换，不仅便捷，而且命名接口使得函数式的使用在设计上更有意义（比如结合设计模式）
>- 一个函数值是否可以赋值给一个函数类型，首先要看函数值的参数类型、参数顺序、返回值类型是否与函数类型声明的形式兼容一致，其次要看函数值声明的Exceptions(Throws子句)能否被函数类型声明的Exceptions(Throws子句)覆盖包含，立即函数-Lambda虽然没有显式的Throws子句，但编译器可以依据其代码中调用的函数的签名汇总出其可能Throw的Ｅxceptions，因此也要符合这个约束条件
>- https://stackoverflow.com/questions/18198176/java-8-lambda-function-that-throws-exceptionss


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

# Clean Code(Stream, Optional)
>- 合理的换行
>- 舍得封装函数
>- 合理封装流式代码
>- 合理使用Ｏptional（map.map.map）
>- 返回Ｓtream或Ｌist？（设计）
>- 少用或者不用并行流

# 三大范式中数据与函数关系
>- 过程式中，数据作为过程入参，数据与函数分离 
>- OOP中，函数作为数据类的方法，可以和数据定义在一起，数据拥有函数
>- 函数式中，通常数据作为函数入参，数据与函数分离，数据只在调用时才与函数结合; 函数式闭包可以在创建函数时就将数据与函数绑定，函数拥有数据


# 函数式的基本模式
### 高阶函数-HOF
### 闭包-Closure
>- 闭包是只有一个方法的匿名类的一个实例，闭包引用的局部变量将Ｃopy到匿名类的成员变量中
>- 依据闭包对其引用的变量的作用以及读写方式，可以将其分为参数配置型（只读，以参数及配置类型而定）变量引用、底层数据型（只读，多为集合型）变量引用以及数据缓存型（读写，多为Ｍａｐ）引用
>- 闭包在实现上是一个结构体或类，它存贮了一个函数（通常是其入口地址）和一个关联的环境（相当于一个符号查找表），环境里是若干对符号和值的对应关系，它既要包括约束变量（该函数内部绑定的符号），也要包括自由变量（在函数外部定义但在函数内被引用），有些函数也可能没有自由变量。闭包和函数最大的不同在于，当捕捉闭包的时候，它的自由变量会在捕捉时被确定，这样即便脱离了捕捉时的上下文，它也能照常运行，捕捉时对于值的处理可以是值拷贝，也可以是名称引用，这通常由语言设计者决定，也可能由用户自行指定（如Ｃ＋＋）[维基百科]
>- Wrap Object
### 柯里化-Currying/Partial
>- 柯里化生成的就是闭包函数，其中被柯里化的参数就是闭包变量
>- 显式命名柯里化，出于设计考虑采用命名柯里化函数，可以提供设计的可理解性（如ILogDecorator.decorate方法）
>- 柯里化工具类，出于重用考虑使用通用柯里化工具类，可以降低代码重复，提供高一致性
# 组合-Compose
>- 组合（静态）是指通过函数定义用代码将一个或多个函数（ＨardＣode）调用编织形成新的函数，一个定义代表一个新的组合。
>- 组合（半动态）是指通过函数定义用代码将一个或多个函数（至少一个作为入参，其他ＨardＣode）调用编织形成新的函数，一次调用创建一个新的组合并调用。
>- 组合（动态）是指定义一个组合函数，在其中通过代码将一个或多个函数（至少一个作为入参，其他HardCode）编织形成新的函数（返回值）并返回，一次调用创建一个新的组合。
>- 静态－ＨardＣode函数，通过定义完成组合，通过执行完成调用；半动态－函数作为入参，通过执行完成组合并调用；动态－函数作为入参以及返回值，通过执行完成组合并返回组合函数，在返回的组合函数上执行完成调用。
### 生成器
### 递归

# 设计模式的函数式实现
### 模板方法
### 策略模式
### 装饰器

# 函数式数据(容器)处理模式
### 封装类型（泛型容器型数据）
>- 泛型集合类型，如List[T]
>- 泛型不相交联合体，如Ｏptinal<T>(Some[T], None), Result<T,Err>(Success<T>, Failure<Err>)
### 问题：函数式如何将各类函数应用于此分装类型
>- 问题一（普通函数）：如何将普通函数应用于包装类型，这就是函子Functor，如Optional.map方法；如将String toString(Integer value)应用于Optional<Integer>。
>- 问题二（返回包装类型的普通函数）：如何将返回包装类型的普通函数应用于包装类型，这就是单子Monad，如Optional.flatMap方法；如将Optional<Integer> toInt(String value)应用于Optional<String>。
>- 问题三（函数包装类型）：如何将函数包装类型中包装的函数应用于应用于另一包装类型，这就是应用子Applicative。
>- 问题四（如何创建函数包装类型）：可以直接用函数构造一个函数包装类型，也可以通过函子操作将一个返回函数的函数应用于普通包装类型，从而将其转换成函数包装类型，返回函数的函数可以自定义也可以是Curry或Partial。
### 范型容器类型的使用模式
>- `像Ｓtream<T>, Optional<T>, Flux<T>, Mono<T>这类函数式范型容器类型，其使用模式基本是：１. 容器类型实例的创建－从其他类型T的实例或Ｓupplier<T>实例化出容器类型；２.利用函数式操作对Ｔ进行各种过滤转换，使被包装类型由Ｔ...Ｒ（可以进行多次转换）（Ｐredicate<M1>，Ｆunction<M1,M2>）；３.终止操作－消费Ｒ类型数据（Ｃonsumer<R>）`

# 为啥函数式编程是声明型编程
>- 不象命令式编程，函数式编程最基本的单位是函数，粒度比语句要大而功能又比较专一，所以以函数组合为最基础模式的函数式编程从代码角度看来必然是函数level的组合以及链式调用，另外基于函数式的框架通过将复杂计算中的控制流与计算抽离从而使得可以通过不同函数的组合（控制流由核心框架固化）实现不同的具体功能，从而提供比命令式编程更具有灵活性和代码重用的编程方法，从代码方面来看，函数式代码可以看做是函数Ｌevel的组合调用，不涉及语句和控制流层面的代码，可以看作是函数的组合调用的声明。