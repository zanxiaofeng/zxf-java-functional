# 函数式类型类知识体系：从合并、变换到效果组合

> 本文是[《Java 函数式编程深度解析：从理论根基到 FP 与 OOP 的融合实践》](java-functional-programming-guide.md)（下称"主篇"）与[《Java 函数式生态与领域建模参考》](java-functional-ecosystem-guide.md)（下称"姊妹篇"）的第三篇。主篇讲 Java 的语言机制（Lambda、Stream、Optional），姊妹篇讲生态里的库与框架，本文补上第三块拼图：**这些容器操作背后的统一理论语言——类型类（Type Class）体系**。主篇 5.2 节的 `Optional.flatMap`、4.4 节的 `Collector`、姊妹篇 3.1 节的 `Validation.combine(...).ap(...)`，在这套语言里都有确切的名字与位置。示例签名采用统一的**伪代码记法**——Scala 风格的类型参数语法加 `×` / `→` 的函数式记法，并非严格的 Haskell / Cats 原生语法（类型类层级与命名以 Haskell base 与 Scala Cats 为准，这两个生态中体系最成体系），每节末尾给出"回望 Java"的衔接注解。

## 目录

- [1. 引言：类型类是一套关于组合的语言](#1-引言类型类是一套关于组合的语言)
- [2. 地基：Semigroup 与 Monoid](#2-地基semigroup-与-monoid)
- [3. 主轴：从 Functor 到 Monad](#3-主轴从-functor-到-monad)
- [4. 遍历维度：Foldable 与 Traversable](#4-遍历维度foldable-与-traversable)
- [5. 对偶与扩展：Comonad 与映射的方向](#5-对偶与扩展comonad-与映射的方向)
- [6. 光学结构：Lens 与不可变数据](#6-光学结构lens-与不可变数据)
- [7. 效果叠加：Monad Transformer](#7-效果叠加monad-transformer)
- [8. 范畴论背景](#8-范畴论背景)
- [9. 总览：类型类全景与 Java 对应](#9-总览类型类全景与-java-对应)
- [10. 总结](#10-总结)
- [11. 时效性说明与状态快照](#11-时效性说明与状态快照)

---

## 1. 引言：类型类是一套关于组合的语言

类型类描述的是**一组类型共同具备的能力**：它类似接口，但有三点关键差异——其一，它更强调"某个类型支持哪些操作"（能力声明），而非"某个对象暴露哪些方法"（行为契约）；其二，它允许**为已有类型补充实例**，不需要修改类型本身的定义；其三，类型类除签名外还附带**定律**（law，如结合律），但定律是文档契约——编译器检查的只是"实例提供了签名要求的方法"，不会（也无法）证明实例满足定律。Cats 的 cats-laws + Discipline 能把定律变成可运行的属性测试，但那是测试期的保障，不是编译期的。这个"类型与能力分离、事后挂接"的设计，是函数式库能为一整套容器统一提供 `map` / `combine` / `traverse` 的前提。

Haskell 用类型类表达这些能力；Scala 的 Cats、TypeScript 侧的 Effect（前身为 fp-ts，现已官方并入 Effect-TS 生态，见第 11 节）等库提供了对应抽象。

理解这套体系时，关键不是背名字，而是看清**每一层解决什么组合问题**：

- 有的负责**合并值**（Semigroup / Monoid）
- 有的负责**在上下文中变换值**（Functor）
- 有的负责**组合多个互相独立的上下文参数**（Applicative）
- 有的负责**链式依赖**（Monad）
- 有的负责**遍历结构**（Foldable / Traversable）
- 有的负责**改变映射的方向与数量**（Contravariant / Profunctor / Bifunctor）

一个先决事实值得放在最前面，且要说得分层才准确：**Java 语言没有原生的类型类机制**——没有 `instance` 语法、没有按约束自动求解实例；并且**没有高阶类型（HKT），无法对"容器构造器本身"做统一抽象**。这两点的影响范围不同：`Semigroup` / `Monoid` / `Eq` 这类**一阶**类型类只作用于具体类型，Java 完全可以用"泛型接口 + 显式传递实例"编码式表达（`interface Semigroup<A> { A combine(A left, A right); }`——第 2 节末 `BinaryOperator` 的对照就是它的无定律投影）；真正受阻的是 `Functor` / `Applicative` / `Monad` 这一**高阶**轴——类型参数的位置上需要一个"接受类型参数的构造器"（`F<?>`），超出了 Java 类型系统的表达力（第 9 节展开）。这不是 Java 的缺陷，而是类型系统的选型差异——但它决定了本文的读法：类型类体系对 Java 开发者的价值不是"直接套用的 API"，而是**给日常已在使用的容器操作（`map`、`flatMap`、`reduce`、`collect`）一套精确的坐标系**，让你能识别手头代码处于哪一层抽象、还缺哪一层、缺的部分要不要手写补齐。

---

## 2. 地基：Semigroup 与 Monoid

在讨论"容器里的值"之前，先要回答一个更基础的问题：**两个值如何合并成一个值？**

### 2.1 Semigroup：可结合的二元合并

**Semigroup（半群）** 要求一个满足**结合律**的二元操作 `combine`：

```text
combine(a, combine(b, c)) == combine(combine(a, b), c)
```

整数与加法、字符串与拼接、集合与求并，都构成 Semigroup。注意表述的精确性：Semigroup 是"类型 + 操作"的整体，不是类型本身——整数配上乘法同样是（另一个）Semigroup。Haskell 用 `newtype Sum` / `newtype Product` 包装来区分同一类型上的多个实例，这正是"类型类 = 能力 + 实例"与"接口 = 方法表"的微妙差别。

一个重要的边界：Semigroup **不要求存在"空值"**。因此"非空列表的拼接"可以是 Semigroup——两个非空列表拼起来仍是非空列表——却不能是 Monoid。

### 2.2 Monoid：有单位元的合并

**Monoid（幺半群）** 在 Semigroup 基础上增加**单位元** `empty`：

```text
combine(empty, x) == x == combine(x, empty)
```

整数加法的 `empty` 是 `0`，字符串拼接的 `empty` 是 `""`，列表拼接的 `empty` 是 `[]`。Monoid 的价值在于**让"折叠"有了起点**：只要元素类型有 Monoid 实例，一个列表就可以被折叠成单个值，而不需要为每种类型重写汇总逻辑——起点恒为 `empty`，步骤恒为 `combine`。

在类型类层级中，通常是：

```text
Monoid extends Semigroup
```

> **回望 Java**：`java.util.function.BinaryOperator<T>` 的形状就是一个 Semigroup 操作（`T × T → T`），但它是**无定律的**——JDK 不承诺结合律，也没有 `empty`。真正的 Monoid 折叠出现在两处：`Stream.reduce(identity, op)`（JDK 文档明确要求 `identity` 必须是累加操作的单位元——违反它会在并行流下产生错误结果，这是 Monoid 定律从纸面进入工程契约的直接实例）；以及 `Collectors.toList()` 这类收集器——"遍历 + 列表拼接"正是 List Monoid 的折叠。Java 没有实例机制，所以"同一类型的多个 Monoid"只能靠命名区分，例如 `BinaryOperator.maxBy()` 与 `minBy()` 实质是两个不同 Semigroup 的构造器。

---

## 3. 主轴：从 Functor 到 Monad

这一条线处理的是"值被放进某种**上下文**（context）之后，如何继续组合"。上下文可以是"可能为空"（`Option`）、"可能失败"（`Either` / `Try`）、"异步"（`Future`）、"多结果"（`List`）。

### 3.1 Functor：在上下文里做 map

Functor 提供的能力是：把一个普通函数应用到上下文内部的值上，**同时保持上下文结构不变**。

```text
map: F<A> × (A → B) → F<B>
```

例如 `Option[Int]` 上的 `map(_ + 1)`：`Some(2)` 变成 `Some(3)`，`None` 仍然是 `None`。列表上的 `map` 逐元素变换且保持列表长度。Functor 捕捉的是"**可映射容器**"这一共性。

Functor 还要遵守两条定律，它们保证 `map` 只做变换、不偷偷改变结构：

```text
map(x, identity) == x                       （恒等律）
map(x, f ∘ g) == map(map(x, g), f)          （复合律）
```

还有一个容易忽略的前提：这些定律中的 `==` 是**数学等价**，不是 Java 的 `==` 或 `equals`。null、副作用、异常与对象身份都会让"等价"在日常 Java 代码里失效——定律的可检验性以纯函数为前提，这也是定律与主篇 2.2 节"纯函数"特征的呼应：一个偷偷写日志的 `map`，谈不上满足恒等律。

### 3.2 Applicative：组合独立的上下文参数

Functor 处理的是"普通函数 + 上下文值"。如果**函数本身也在上下文里**，就需要 Applicative：

```text
ap: F<A → B> × F<A> → F<B>
pure: A → F<A>        （把普通值提升进上下文）
```

Applicative 的典型用途是**组合多个互相独立的上下文参数**。两个 `Option` 可以用 `mapN` 组合：都为 `Some` 时得到组合结果，任一为 `None` 时整体为 `None`。这里的"**独立**"是关键定语：后一个参数的计算**不依赖**前一个参数的结果——参数之间没有数据依赖。但两个常见推论都必须收着说。**其一，独立不等于可并行**：Applicative 的结构为并行提供了空间，却不承诺并行执行——参数即使没有数据依赖，副作用也可能冲突（写同一个文件、争用同一把锁、要求互斥访问同一资源），因此像 `IO` 这样的实例通常**顺序**求值，"是否并行"是具体实例与执行策略层的事。**其二，错误累积不是 Applicative 的通用保证**，而是特定实例的选择：

- `Option` / `Either` 的 `mapN`：**短路**——任一失败即整体失败，后续校验不再执行；
- `Validated` 的 `mapN`：**累积**——所有校验都执行，错误用 `Semigroup` 合并上报（注意只需要错误类型有 Semigroup——合并用不到单位元，还谈不上 Monoid）；
- `IO` / `Future` 的 `mapN`：执行所有效果，失败语义随实例（`Future` 短路在第一个异常）。

把"`Validated` 的累积"说成"Applicative 的累积"，就如同把"`Future` 的异步"说成"Applicative 的异步"——都是把实例性质误当成层级保证。

能力上，Applicative 强于 Functor：如果 F 是 Applicative，就可以用 `ap` 和 `pure` 实现 `map`：

```text
map(fa, f) == ap(pure(f), fa)
```

反方向，每个 Monad 也都能用 `flatMap` + `map` 派生出 `ap`（`ap(ff, fa) == flatMap(ff, f -> map(fa, f))`）。因此"某个类型是不是 Applicative"问的往往不是能力有无，而是**接口暴露了什么、实例语义选择了什么**——3.3 节的 `Validated` 正是后者的典型。

### 3.3 Monad：链式依赖与上下文压平

Monad 在 Applicative 之上增加了"**根据前一步结果决定下一步**"的能力，并把嵌套上下文压平：

```text
flatMap: F<A> × (A → F<B>) → F<B>
```

对比一下签名就能看清分界线：Applicative 的参数在进入组合前就已固定；Monad 的下一步函数 `A → F<B>` 拿到**上一步的值**才能产生下一步的上下文。这适合**依赖式链式计算**：先查用户，可能为空；查到用户后再以其 ID 查订单，也可能为空；任何一步失败就整体短路。`Option`、`Either`、`CompletableFuture`、IO 的链式操作，本质上都对应 `flatMap` 或类似操作（JavaScript `Promise.then` 把 map 与 flatMap 合并成一个自动解包的方法，是"类似"而非严格对应）。

Monad 同样有定律，三条写全（记法与前文一致）：

```text
flatMap(pure(a), f) == f(a)                                     （左单位）
flatMap(m, pure) == m                                           （右单位）
flatMap(flatMap(m, f), g) == flatMap(m, x -> flatMap(f(x), g))  （结合律）
```

结合律可浓缩为一句：链式展开的结果必须与"先组合再展开"一致——`flatMap` 真的消除了嵌套，而不是再包一层。此外 Monad 的操作与 Applicative 必须保持一致：`ap(pure(f), m) == map(m, f)`，同一类型的两套 API 不能各说各话。值得澄清的是"`flatMap` 就是压平"这个常见印象：`flatMap` ≡ `map` + `join`（`join: F<F<A>> → F<A>`），压平只是表象；真正的能力增量是**序列化**——后一步的函数拿到前一步的**值**才能构造下一步计算，"能压平"是这个依赖能力的副产品。

这层依赖能力也解释了一个经典设计取舍：**`Validated` 为什么停留在 Applicative 层、不提供 Monad 实例**。`flatMap` 的语义是"拿到上一步的结果才能决定下一步"，失败时自然短路——这与错误累积直接冲突（累积要求失败后继续执行其余校验）。所以 Cats 的 `Validated` 刻意不实现 Monad，需要链式时经 `Either` 桥接（`toEither` 转过去 `flatMap`，再转回来）；`flatMap` 与错误累积的冲突由签名决定，是实现绕不开的结构性矛盾，不是工程不便。

三者关系通常写成：

```text
Functor ⊂ Applicative ⊂ Monad
```

可以概括为：**Functor 只能变换；Applicative 能组合多个独立上下文参数；Monad 能根据前一步结果决定后一步。能力逐层增强，适用范围逐层缩小**——每个 Monad 都是 Applicative，每个 Applicative 都是 Functor；反过来不成立。

> **回望 Java**：这一轴在 JDK 里的对应物最完整。`Optional.map` / `Stream.map` / `CompletableFuture.thenApply` 是 Functor（`map`）；`Optional.flatMap` / `Stream.flatMap` / `CompletableFuture.thenCompose` 是 Monad（`flatMap`/bind；主篇 5.2 节的链式空值处理、7.3 节手写的 `Result` 管道都是 Monad 链）。Applicative 这一层要分两句话说：JDK 缺的是**统一命名的抽象**——没有横跨容器的 `pure` / `ap` / `mapN` 接口；但**组合能力散落在具体容器上**，`CompletableFuture` 就是 JDK 里 Applicative 结构最完整的容器：
>
> | 类型类操作 | `CompletableFuture` 对应 |
> |---|---|
> | `pure` | `completedFuture(x)` |
> | `map` | `thenApply(f)` |
> | `map2` | `thenCombine(other, fn)` |
> | `ap` | `f.thenCombine(x, (fn, a) -> fn.apply(a))`（派生） |
> | `flatMap` | `thenCompose(f)` |
>
> `CompletableFuture.completedFuture(20).thenCombine(CompletableFuture.completedFuture(22), Integer::sum).join()` 得到 `42`，就是一次 map2。两个边界要说清：其一，`thenCombine` 组合的是**结果**——两个上游何时、如何执行由各自的异步构造决定，`thenCombine` 本身不承诺并行；其二，它的失败语义是任一异常即整体以 `CompletionException` 失败，**不累积**错误。`Optional` 则是另一半现实：有 Functor/Monad 层的方法，却没有 mapN/组合操作——多个 `Optional` 参数的独立组合只能嵌套 `map`（得到 `Optional<Optional<...>>`）或手写。这正是"缺统一抽象"的落点形态：**每个容器补齐哪一层是各自的历史决定，没有系统性**。也因此，嵌套 `flatMap` 表达独立组合的代价是明确的：退化为 Monad 写法、丢失"参数独立"的结构语义（3.2 节：独立只是并行的必要条件）。库侧最接近的现成实现是姊妹篇 3.1 节 Vavr 的 `Validation.combine(...).ap(...)`——它正是用 Applicative 的结构实现错误累积（全部校验都执行、错误合并），而 `Either.flatMap` 链只会在第一个失败处短路。**"错误累积选 Applicative、错误短路选 Monad"**，这是类型类层级直接指导 API 设计的一个实例。

---

## 4. 遍历维度：Foldable 与 Traversable

另一条线关心的是**结构如何被"走过"**。

### 4.1 Foldable：消费形状

Foldable 抽象"把结构折叠成一个摘要值"的能力。列表的 `sum`、`reduce`、`foldLeft` 都属于这一类。它与 Monoid 天然配套：只要元素类型有 Monoid 实例，Foldable 就可以把整个结构折成一个值——`foldMap` 的签名把这件事说得很直白：

```text
foldMap: F<A> × (A → M) → M        （要求 M 有 Monoid 实例）
```

### 4.2 Traversable：保留形状地组合效果

Traversable 比 Foldable 多保留一样东西：**结构的形状**。它在遍历时把每个元素"效果化"，同时保持整体骨架：

```text
traverse: F<A> × (A → G<B>) → G<F<B>>        （要求 G 有 Applicative 实例）
```

两个细节值得停一下。其一，返回类型 `G<F<B>>` 是一次**翻转**：效果从"元素级"被提到了"整体级"。其二，约束写在 G 上——G 必须是 Applicative，而 Applicative 恰好提供"组合多个独立效果"的机制，`traverse` 的能力正是逐元素效果 + Applicative 组合的乘积。

典型例子是把 `List[Option[Int]]` 翻转为 `Option[List[Int]]`（在 Cats 里这个纯翻转叫 `sequence`，即 `traverse(identity)`）：如果所有元素都是 `Some`，就得到 `Some(List(...))`；只要有一个 `None`，整体就是 `None`。注意"保形"的确切含义：**成功时结构完整保留；失败时结果里没有结构**——`None` 不携带长度、也不标记失败位置，"要么完整结构、要么无结构"，不存在半截列表。

失败时发生什么、保留多少信息，**由效果类型 G 决定**：G 是 `Option` 时短路在第一个 `None`；G 是 `Validated` 时累积所有错误（与 3.2 节的实例三分一致）；G 是 `List` 时得到所有成功组合的笛卡尔积（非确定性计算）。这与 `map` 形成约束上的对照：`map` 只要求 F 是 Functor，`traverse` 额外要求 G 是 Applicative——"遍历 + 效果组合"的能力，后一半正是从 G 借来的。

一句话对比：**Foldable 消费形状，Traversable 保留形状并组合效果**。

> **回望 Java**：`Stream` 天生是 Foldable（`reduce` / `collect`，见第 2 节末注解），但 Traversable 在 JDK 中没有对应物，`List<Optional<T>> → Optional<List<T>>` 的翻转是 Java 的经典手写场景：
>
> ```java
> static <T> Optional<List<T>> sequence(List<Optional<T>> list) {
>     List<T> out = new ArrayList<>(list.size());
>     for (Optional<T> e : list) {
>         if (e.isEmpty()) return Optional.empty();   // 任一为空，整体为空
>         out.add(e.orElseThrow());
>     }
>     return Optional.of(out);
> }
> ```
>
> 常见的流式替代 `list.stream().flatMap(Optional::stream).toList()` 有一个隐藏损失：它**丢掉了"是否曾有元素为空"的信息**，语义不再等价。识别出"这是一个 Traversable 场景"，就知道手写时必须保留两个不变量：形状（列表长度）与效果（任一空则整体空）——这正是理论坐标系对手写代码的指导价值。

---

## 5. 对偶与扩展：Comonad 与映射的方向

### 5.1 Comonad：从上下文中取出并消费

Monad 是"把值放进上下文并链式产生新上下文"。**Comonad 是它的数学对偶**：从上下文中**提取**值，并在上下文上链式消费：

```text
extract: W<A> → A
extend:  W<A> × (W<A> → B) → W<B>
```

`extract` 对应 Monad 的 `pure`（方向相反：一个是提升进去，一个是提取出来），`extend` 对应 `flatMap` 的反向形式。Comonad 适合"**总是存在当前焦点**"的场景：非空列表的 zipper（拉链，带游标的遍历结构）总能 `extract` 出焦点元素；一个信号流总能从当前信号取出值，也能基于整个信号历史产生新信号。

> **回望 Java**：JDK 没有 Comonad 的直接对应（`Optional.get` 形似 `extract`，但 `Optional` 可为空，不满足"总存在焦点"的前提）。它对 Java 开发者主要是**识别镜面**：当你在响应式流上做"基于窗口历史计算指标"（如滑动平均）时，手里的结构概念上是 Comonad 形态的。

### 5.2 Bifunctor：同时映射两个类型参数

Functor 只映射一个类型参数。**Bifunctor** 能同时变换两个，`Either[E, A]` 的 `bimap` 可同时变换 `Left` 与 `Right`。它适合"两种分支都值得映射"的结构——错误轨道与成功轨道都需要变换时，`bimap` 一趟完成。

> **回望 Java**：Vavr 的 `Either.mapLeft().map()` 组合即是这个能力的实例化（姊妹篇 3.1 节）；JDK 的 `Optional` 只有一个类型参数，不存在此问题。

### 5.3 Contravariant 与 Profunctor：改变映射的方向

普通 Functor 是正向映射：

```text
map: F<A> × (A → B) → F<B>
```

**Contravariant（逆变换函子）** 的方向相反：

```text
contramap: F<B> × (A → B) → F<A>
```

它描述的是"**消费者**"而不是"容器"：消费者消费 B，若给它一个 A → B 的前处理，就得到一个消费 A 的消费者。典型例子：`Predicate[A]` 通过 `contramap` 一个 `B → A` 变成 `Predicate[B]`——不是把值变过去，而是把**判断逻辑接上去**。

**Profunctor** 对输入和输出**两个位置**都做映射（输入逆、输出正），函数类型 `A → B` 本身就是它的原型：

```text
dimap: P[A, B] × (A' → A) × (B → B') → P[A', B']
```

Profunctor 是理解 Lens 等光学结构的底层语言之一（见下节）。

> **回望 Java**：`java.util.function.Predicate.negate()` / `and()` 是消费者组合的雏形，但没有 `contramap`——JDK 无法对一个已构造的 `Predicate<B>` 前接 `A → B` 得到 `Predicate<A>`，只能手工包一层 `a -> pred(f(a))`。识别出 Contravariant 模式后，"先包一层再组合"就有了名字与章法。

---

## 6. 光学结构：Lens 与不可变数据

Lens 不是 Monad 那一系的类型类，而是一种**光学结构（optics）**。它把"从大结构中取出小字段"和"把小字段的新值放回大结构"打包成一个**可组合的值**：

```text
Lens[S, A] ≈ (get: S → A, set: (S, A) → S)
```

关键在于**组合**：从 `Person` 到 `Address` 的 Lens，可以和从 `Address` 到 `Street` 的 Lens 组合成从 `Person` 直接到 `Street` 的 Lens——而整个过程不修改原数据，`set` 返回新结构。这使"深路径更新"从层层解构变成一行组合调用，正是不可变数据（主篇 2.2 节的第三特征）在工程上可行的配套工具。

现代函数式库中，Lens、Prism（聚焦可变体分支）、Traversal（聚焦多焦点）、Iso（双向同构）常被统称为光学结构，它们通常通过 Profunctor 等抽象实现（profunctor optics）——上节"Profunctor 是底层语言"即指此。

> **回望 Java**：record + 模式匹配（主篇 9.2 节）覆盖了 `get` 方向的解构，但"深路径不可变更新"仍需 `withX(...)` 样板层层展开。Java 生态没有主流 Lens 库；姊妹篇 3.1 节 Vavr 的持久化集合配合手写 `with` 方法是最接近的实践。此处理论的价值同样是**命名**：当"改一个嵌套字段要重建三层对象"的样板反复出现时，你知道它在别的生态里叫 Lens，有成熟的组合方案可借鉴。

---

## 7. 效果叠加：Monad Transformer

真实程序往往同时需要多种效果，例如"异步 + 错误处理"或"状态 + IO"。**Monad Transformer** 的思路是在一个 Monad 之上叠加另一个 Monad 的能力。例如 `OptionT[Future, A]` 在 `Future` 之上叠加了"可能为空"的效果。它不是一种新的 Monad，而是**一族构造器**：给定基础 Monad，产生增强版 Monad。

需要如实说明的一点：Transformer 的组合顺序影响语义（`EitherT[Option, ...]` 与 `OptionT[Either, ...]` 语义不同），且嵌套越深、类型签名越重——这是 Cats 生态后来发展出 Tagless Final 等替代方案的现实背景。

此外还有两个更高级的组合工具，属于理解前面核心层级之后的进阶内容：

- **Free Monad**：把程序逻辑构建成纯数据结构（语法树），再交给解释器执行，从而实现**业务逻辑与执行方式的解耦**——一个"程序"，多个"解释器"（真实执行 / 测试桩 / 日志回放）。
- **Arrow**：比 Monad 约束更弱、沿另一条轴泛化的计算管道抽象——正因为约束更弱，它容纳了一些不是 Monad 的类型（如某些流式自动机），适合组合"结构不依赖中间值"的复杂输入输出管道（解析器组合子是经典应用）。

> **回望 Java**：JDK 没有 Transformer 的通用机制（缺高阶类型，见第 9 节），但"多效果叠加"的需求真实存在，Java 的惯用解法有三条路：其一，嵌套具体容器并手工解包——`CompletableFuture<Optional<T>>` 上 `thenCompose` 与 `orElse` 无法直接组合，工程上常退回命令式外壳（主篇 7.1 节"函数式核心，命令式外壳"正是这种退路的架构化）；其二，选用把特定效果组合**特化**进单一类型的库——如 Vavr 的 `Try` 把"计算 + 异常"并入一个容器——但这是逐案特化，不是通用叠加；其三，参照完整实现——Vavr 同样未提供通用 Transformer 家族（社区曾有 [`TryOptionT` 原型提案](https://github.com/vavr-io/vavr/issues/1868) 但未收编），JVM 上成体系的 Transformer 要到 Scala/Cats 里找，Java 侧最接近的对照阅读是姊妹篇 3.4 节 Functional Java 的经典 Monad 实现。

---

## 8. 范畴论背景

这套类型类呈现"函子—应用子—单子"的层级，根源在范畴论。粗略地说：

- **函子（functor）** 是范畴之间的映射，把对象映到对象、态射映到态射，并保持复合与单位——这正是 Functor 两条定律的来历；
- **自然变换（natural transformation）** 是函子之间的映射，在两个容器之间转换并保持结构——`sequence`（4.2 节）的整体翻转 `F[G<A>] → G<F<A>]` 在这个视角下就是一个自然变换；
- **Monad 可以理解为自函子范畴上的 Monoid**（Mac Lane 的经典表述）：`pure` 是单位元，`join`（压平，`flatMap` 的另一半）是乘法——第 2 节的"合并"与第 3 节的"链式"在此汇合。

Haskell 直接把这种层级写进类型类定义（自 GHC 7.10 / 2015 年起）：

```haskell
class Functor f     => Applicative f where ...
class Applicative m => Monad m       where ...
```

同理，`Monoid` 自 GHC 8.4 / 2018 年起以 `Semigroup` 为超类。Scala 的 Cats 用 trait 继承表达相同的关系，且层级分得更细（`trait Monad[F[_]] extends Applicative[F] with FlatMap[F]`，在 Functor 与 Applicative 之间还细分出 `Apply`、`FlatMap` 等中间层，主线不变）。这些年份说明一件事：**层级是语言逐步精化出来的，不是一天设计成的**——Java 生态今天面临的"哪些操作该有什么关系"，Haskell 生态用了二十年才收敛到当前形态。

---

## 9. 总览：类型类全景与 Java 对应

把全文收拢为一张总表。第四列是"操作在 Java 中的落点"——注意 Java 的落点是**散落在各个具体容器上的同名方法**，而非统一的抽象：

| 类型类 | 核心操作 | 解决的组合问题 | Java 中的落点 |
|--------|----------|----------------|----------------|
| Semigroup | `combine` | 值与值合并（结合律） | `BinaryOperator`（无定律承诺） |
| Monoid | `empty` + `combine` | 有起点的合并、折叠 | `reduce(identity, op)`、`Collectors.toList()` |
| Functor | `map` | 上下文内变换，保持结构 | `Optional.map` / `Stream.map` / `thenApply` |
| Applicative | `pure` + `ap` | 组合**独立**上下文参数（失败语义与是否并行由实例决定） | `CompletableFuture` 的 `completedFuture` / `thenCombine`（pure/map2 形态）；Vavr `Validation.combine().ap()`——均无统一接口 |
| Monad | `flatMap` | 依赖式链式组合、压平 | `Optional.flatMap` / `Stream.flatMap` / `thenCompose` |
| Foldable | `foldMap` / `fold` | 折叠消费结构 | `Stream.reduce` / `collect` |
| Traversable | `traverse` / `sequence` | 保形遍历 + 效果组合 | 手写 `sequence`（4.2 节） |
| Comonad | `extract` + `extend` | 从上下文取出并消费（对偶） | 无直接对应 |
| Bifunctor | `bimap` | 双类型参数同时映射 | Vavr `Either.mapLeft` |
| Contravariant | `contramap` | 消费者方向的反向映射 | 手工包装 `a -> pred(f(a))` |
| Profunctor | `dimap` | 输入输出双向映射；optics 底座 | 无直接对应 |
| Lens 等光学结构 | `get` / `set` / 组合 | 不可变数据的可组合深焦点 | 手写 `withX` 样板（无组合机制） |
| Monad Transformer | `OptionT[F, A]` 等 | 多效果叠加 | 合并型容器（`Promise<Result<T>>`）或命令式外壳 |

**为什么 Java 无法把左列做成统一抽象？** 根源是 Java 泛型不支持高阶类型（Higher-Kinded Types，HKT）：无法写出"对任意容器构造器 F 抽象"的接口。一个 Functor 类型类在 Java 里的理想形态是这样的（**无法编译**）：

```java
// 伪代码：Java 不存在 F<?> 这样的类型参数——F 本身必须是"接受一个类型参数的构造器"
interface Functor<F<?>> {
    <A, B> F<B> map(F<A> fa, Function<A, B> f);
}
```

`F` 作为"类型构造器"出现在类型参数位置，这超出了 Java 类型系统的表达力。因此 `Optional.map`、`Stream.map`、`thenApply` 三者**结构同构却无法被同一个接口统一**——这就是本文开头所说"坐标系而非 API"的含义：Java 开发者消费的是类型类体系在单个容器上的**投影**，而无法抽象容器本身（一阶类型类的"接口 + 实例"编码不受此限，见第 1 节；受阻的只是这条高阶轴）。Scala 原生支持 `F[_]`，Cats 由此把整张表做成了一等公民；这个差异也是姊妹篇"选型哲学"（范式不依赖库）在类型层面的注脚——Java 团队借用的是**分层思想**，不是库。

---

## 10. 总结

这套体系可以这样记忆：

- **Semigroup / Monoid**：负责合并值——结合律 + 单位元，折叠的起点。
- **Functor**：负责在上下文中变换值——结构不变。
- **Applicative**：负责组合多个**独立**上下文参数——独立只是结构事实；错误累积、短路还是并行，由实例决定。
- **Monad**：负责依赖式链式组合并压平上下文——上一步的值决定下一步。
- **Foldable**：负责折叠消费结构——形状用完即弃。
- **Traversable**：负责保持形状的遍历与效果组合——形状与效果都要。
- **Comonad**：负责从上下文中取出并消费——Monad 的镜面。
- **Bifunctor / Contravariant / Profunctor**：负责映射的数量与方向——容器之外还有消费者。
- **Lens 等光学结构**：负责不可变数据的可组合焦点。
- **Monad Transformer**：负责叠加多种效果。

它们不是互相替代的名词，而是一套**能力逐层叠加、方向彼此对偶**的组合语言。对 Java 开发者，掌握它的意义有两层：**识别**——当代码中反复出现"判空—继续—短路"（Monad）、"遍历—收集—任一失败即整体失败"（Traversable）、"合并—汇总"（Monoid）、"多个校验都要跑完并汇总错误"（Applicative）时，能识别它属于哪一层抽象；**补齐**——知道 JDK 缺的是**统一抽象**这一层（Applicative、Traversable、Lens 没有跨容器的统一接口；具体能力散落在各容器上，如 `CompletableFuture.thenCombine` 就是 map2 形态），缺的部分何时值得手写样板、何时该退回命令式外壳。这套坐标系与主篇的机制、姊妹篇的生态合成完整的三视角：**机制（怎么用）、生态（用什么）、理论（是什么与缺什么）**。

---

## 11. 时效性说明与状态快照

本文涉及的库状态核查于 **2026 年 11 月**，来源为各项目官方渠道：

- **fp-ts / Effect**：fp-ts（gcanti）已官方并入 Effect-TS 生态，仓库 README 明确 Effect-TS 是 fp-ts v2 的继任者（"embodies what would be considered fp-ts v3"）；fp-ts v2 仍可维护使用，但新项目应首选 [Effect-TS](https://effect.website)。TypeScript 侧接触类型类体系，现应以 Effect 的文档为准。
- **Cats**：2.13.0（2025 年 1 月发布），支持 Scala 2.12 / 2.13 / 3.3 及 Scala.js / Scala Native，Typelevel 生态持续活跃维护——本文所述 trait 层级（`Monad extends Applicative extends Functor`）以该版本为准。
- **Haskell base**：`Functor => Applicative => Monad` 超类链自 GHC 7.10（2015）确立；`Monoid` 以 `Semigroup` 为超类自 GHC 8.4（2018）确立。类别层级属于稳定事实，不随小版本变化。
- Java 侧引用（Vavr、Functional Java、Pragmatica 等）的状态见姊妹篇第 7 节核查快照，本文不重复。

---

*本文为《Java 函数式编程深度解析》系列的第三篇（理论篇），三篇独立成文、交叉引用。文中库状态以第 11 节核查日期为准；类型类定律表述为通行教学版本，严格形式以各库官方文档为准。*
