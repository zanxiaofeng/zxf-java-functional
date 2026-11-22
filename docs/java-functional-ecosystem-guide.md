# Java 函数式生态与领域建模参考：基础库、框架与实战代码库

> 本文是[《Java 函数式编程深度解析：从理论根基到 FP 与 OOP 的融合实践》](java-functional-programming-guide.md)（下称"主篇"）的姊妹篇。主篇走的是"语言机制 → 管道 → 模式 → 实战"的主线，示例以手写代码为主；本文回答随之而来的下一个问题：**走出手写示例之后，Java 生态里有哪些可以直接引用的库、框架和完整参考实现？** 每个条目都给出它与主篇章节的衔接点，关键库配有最小代码示例（Vavr 0.11.0 与 jOOλ 0.9.16 的示例经真实依赖编译运行验证；Vavr 1.x 线官方定位为 0.11.0 的直接替代，本文示例未在 1.x 线重新验证），并对成熟度与维护状态如实说明（核查快照与验证级别见文末第 7 节）。

## 目录

- [1. 选型哲学：库是加速器，范式不依赖库](#1-选型哲学库是加速器范式不依赖库)
- [2. 生态总览](#2-生态总览)
- [3. 函数式基础库](#3-函数式基础库)
- [4. 函数式领域建模框架](#4-函数式领域建模框架)
- [5. 完整参考实现](#5-完整参考实现)
- [6. 选型决策：按场景而非按流行度](#6-选型决策按场景而非按流行度)
- [7. 时效性说明与状态快照](#7-时效性说明与状态快照)

---

## 1. 选型哲学：库是加速器，范式不依赖库

主篇 7.3 节的结尾说过一句话："库是语法糖加速器，范式不依赖它"。这句话是本文所有推荐的前提，值得先展开成三条选型原则：

**原则一：先问语言给了什么，再问库补了什么。** Java 21 的 switch 模式匹配转正、Java 24 的 Stream Gatherers（主篇 9.1）落地之后，许多曾经需要库才能获得的能力——模式匹配、窗口操作、scan/fold——已经有了原生表达。库的推荐价值必须放在"当前 JDK"的坐标系里重新评估：**Vavr 的 `Match` API 在 switch 模式匹配时代的必要性大幅下降；jOOλ 的 `windowFixed` / `windowSliding` 类固定与滑动切窗已被 Gatherers 收编**——但 jOOλ 的 SQL 风格 `window`（分区、排序、rank / lead / lag、窗口聚合）不在 Gatherers 的覆盖范围内（3.2 节展开）。后文的推荐都会标注这类"已被原生能力覆盖"的退化项。

**原则二：依赖是有半径的。** 引入一个函数式库，意味着团队心智模型、IDE 支持、调试体验、新成员上手成本都会随之变化。Vavr 带来的是一个完整范式工具箱，jOOλ 带来的是几个 Stream 补丁，代价完全不同。"轻量 → 完整"的梯度不是为了排序好坏，而是为了让不同半径的团队各取所需。

**原则三：领域建模框架的"实验性"要如实告知。** 函数式领域建模在 Java 侧的生态远不如 Kotlin/Rust 侧成熟，几个最纯粹的项目（如 fmodel-java）明确标注 experimental。它们的价值是**概念示范与设计灵感**——把书里的思想翻译成可运行的 Java 代码——而不是"加进 pom 就能用的生产依赖"。本文对每个此类项目都会原文引用其成熟度声明。

---

## 2. 生态总览

| 名称 | 坐标 / 仓库 | 定位 | 基线 | 状态快照（2026-11） | 推荐角色 |
|------|------------|------|------|---------------------|----------|
| Vavr | `io.vavr:vavr`（当前 1.1.0，2026-10） | 完整 FP 工具箱：持久化集合、Option/Try/Either、Validation、Match | Java 8+（1.x 线） | 0.11.0（2025-12）后重回活跃：1.0.0 / 1.0.1 / 1.1.0 相继发布；1.x 线保守稳定，现代化改动规划在 2.x 线 | 生产可用，首选基础库 |
| jOOλ | `org.jooq:jool:0.9.16`（Java 8 环境用 `jool-java-8` 同版本） | 补全 JDK Stream 缺失操作：zip、foldLeft、SQL 风格 window、元组 | 主 artifact 构建目标 Java 11；`jool-java-8` 为 Java 8 发行物 | 0.9.16，发布节奏慢但 API 极稳定 | 轻量增强，可选 |
| Pragmatica core | `org.pragmatica-lite:core:1.0.0-rc4` | 小而新的 `Result` / `Option` / `Promise` 三类型库 | **Java 25** | 1.0.0-rc4（尚未 GA），项目年轻 | 观察项 / 前沿尝试 |
| Cyclops | `com.oath.cyclops:cyclops:10.4.1` | 函数式 + 响应式混合（ReactiveSeq、AnyM 统一抽象） | Java 8+ | 10.4.1（2022-05）后无发布，**实质休眠** | 仅了解，不推荐新项目 |
| Functional Java | `org.functionaljava:functionaljava:5.0` | 学术风格完整 Monad 实现（IO、State、Reader 等） | Java 8+ | 5.0（2022-02）后无发布 | 概念对照，脚注级 |
| fmodel-java | `fraktalio/fmodel-java` | Decider 纯函数领域组件的 Java 实现 | 构建目标 Java 23（源码 0.0.1-SNAPSHOT） | README 自述 **experimental，未发布到 Maven Central** | 概念示范，非生产依赖 |
| Dewdrop | `matsientst/dewdrop` | 注解驱动事件溯源框架 | Java 11+ | 官网自述"早期阶段，尚未生产就绪"，仅支持 EventStoreDB | 对照参考 |
| jOOQ | `org.jooq:jooq`（按 edition × 数据库 × 版本授权） | 类型安全 SQL DSL，`fetchStream()` 可作函数式管道数据源 | 3.20+（2025-02 起）开源线要求 Java 21；商业版另有 Java 17/11 发行物 | 持续活跃维护（3.21 线已至 3.21.10） | 生产可用，数据源接入 |
| functional_domain_modeling_in_java | `horie-t/programming-study` 子目录 | 《Domain Modeling Made Functional》的 Java 25 移植实战 | Java 25 | 个人学习仓库 | 完整参考实现 |
| resilience-wallet-ledger | `fResult/resilience-wallet-ledger` | Kotlin + Vavr 的 Railway Oriented Programming 实战 | Kotlin / JDK 24 | 个人沙盒（作者声明不接受 PR、不提供支持） | 跨语言参照 |

> 表中"状态快照"的核查依据与日期见第 7 节。版本与活跃度会变化，引入前请复核。

---

## 3. 函数式基础库

### 3.1 Vavr：最完整的函数式工具箱

**衔接点**：主篇 7.3 节手写的 `Result`、6.4 节的校验管道、2.2 节的不可变性讨论，在 Vavr 里都有生产级的对应物。

Vavr（前身 Javaslang）是 Java 8+ 上最成熟的函数式库，四个核心资产：

**（a）`Try`：把异常捕获变成管道操作。** 主篇 7.3 节手写的 `Result` 需要调用方显式构造成功/失败；`Try` 直接把"会抛检查异常的调用"包装进类型系统：

```java
// Try.of 接受一个可能抛异常的 lambda，异常被捕获为 Failure 而非向上传播
Try<PaymentResponse> attempt = Try.of(() -> gateway.charge(request))
    .recover(GatewayTimeout.class, e -> fallbackResponse)  // 按异常类型恢复
    .map(this::enrichResponse);                             // 仅在成功轨道上映射

PaymentResponse resp = attempt.getOrElseThrow(
    e -> new PaymentException("支付失败", e));              // 在边界处统一还原为异常
```

这正是主篇 7.3 节"函数式错误处理管道"的现成版本——区别在于 `Try` 的失败轨道固定承载 `Throwable`，而手写 `Result<T, E>` 的 `E` 可以是任意领域错误类型（配合主篇的 ADT 错误谱系）。

用 `Try` 表达这类管道，三条运行时边界要预先说清。其一，**`recover` 用在支付超时场景的业务语义**：超时不是确定的失败——款可能未扣、可能已扣但响应丢失、状态未知，因此 fallback 值不能伪装成"支付成功"或确定的失败，更不能据此直接重试扣款；正确的形态是表达"待确认 / 处理中"并配合幂等键与结果查询（若只是想演示 `recover` 的机制，换一个低风险的读取场景更合适）。其二，**`Try.of` 是立即求值的**——传入的 lambda 当场执行，扣款副作用已经发生，后续 `map` 抛出异常也不会撤销它。其三，**并非所有 `Throwable` 都会成为 `Failure`**：`InterruptedException`、`LinkageError`、`ThreadDeath`、`VirtualMachineError`（含 `OutOfMemoryError`、`StackOverflowError`）被 Vavr 视为致命异常，会从 `Try.of` 调用处直接抛出（0.11.0 与 1.x 行为一致，源码 `isFatal` 判断）。

**（b）`Either` / `Validation`：快速失败与错误累积。** `Either<L, R>` 与手写 `Result` 语义一致（`map`/`flatMap` 走右轨道）；更有差异化价值的是 `Validation`——主篇 6.4 节的校验管道用 `flatMap(Optional::stream)` 收集全部错误，`Validation` 把这个模式做成了内建能力：

```java
// 错误累积：三个校验器全部执行，错误合并为 Seq，而不是在第一个失败处短路
Validation<Seq<String>, ValidatedRequest> validated = Validation.combine(
    validateCustomerId(request),   // Validation<String, CustomerId>
    validateItems(request),        // Validation<String, List<OrderLine>>
    validateAddress(request)       // Validation<String, Address>
).ap(ValidatedRequest::new);       // 全部成功时组合为 ValidatedRequest
```

**（c）持久化集合。** `io.vavr.collection.List` / `Map` / `Set` 是真正的持久化数据结构（结构共享，"修改"返回新集合），比 `List.of()` 的不可变包装更适合函数式管道中的中间态传递。

**（d）`Match` 模式匹配——一项已经退化的能力。** `Match(x).of(Case($(1), "one"), ...)` 曾是 Java 21 之前 Vavr 的刚需卖点；switch 模式匹配 + record 解构转正后（主篇 6.6、9.2），原生 switch 穷举性更强（编译器检查 sealed 全集），新代码应优先用原生语法。

**如实的状态说明**（按版本线区分）：Vavr 曾在 2021–2024 年间长期休眠（0.10.4 到 0.10.5 隔了近三年），2025 年底发布 0.11.0 后重回活跃——0.11.0 发布说明的原话是"0.11.x 线继续收缺陷修复"，且"焦点转向 1.0.0，从显著提升 Java 版本基线开始"；此后 1.0.0 / 1.0.1 / 1.1.0 相继发布（Maven Central，2026-10 记录 1.1.0）。1.0.0 发布说明把项目定位说得很清楚：**"production-ready, stable, and not dead"**——1.x 线是 0.11.0 的直接替代（drop-in，仅移除实验性 Task API）并**保持 Java 8 兼容**；更激进的变化（Java 基线提升、API 重设计、采用现代 JVM 特性）被**明确规划在 2.x 线**。因此"它会不会跟进 sealed interface、record"的准确答案是：1.x 线保守不动，2.x 线才是它的现代化窗口。本文示例在 0.11.0 上验证；按官方 drop-in 声明预期 1.x 兼容，升级后建议自行回归。

### 3.2 jOOλ：给原生 Stream 补齐的最后几块拼图

**衔接点**：主篇第 4 章 Stream API 的"延伸阅读"。

jOOλ（`org.jooq.lambda.Seq`）只做一件事：补全 JDK Stream 缺失的操作。`Seq` 本身就实现了 `Stream` 接口——`Seq.seq(stream)` 把现有 Stream 升级为 `Seq`，升级后的对象可直接传给任何接受 `Stream` 的 API，**类型层面的互操作很顺滑**。但"顺滑"不等于"零成本"：`Seq` 是**顺序、有序**的 Stream 扩展——并行管道应退回普通 `Stream` 处理，流的关闭与一次性消费语义也一切照旧，这些约束不会因为类型兼容而消失：

```java
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

// zipWithIndex：JDK Stream 至今缺失的"带索引遍历"
// Tuple2 的 v1/v2 是字段访问——Java 语言层面没有元组解构
Seq<Tuple2<String, Long>> indexed = Seq.of("a", "b", "c").zipWithIndex();

// foldLeft：顺序左折叠（主篇 6.6、7.2 节：JDK 的 3 参数 reduce 语法上可表达折叠，
// 但其 combiner 契约在顺序折叠语义下无法满足，正确的手写形态是显式循环——
// foldLeft 的价值正是把这条顺序约束直接写进库语义）
int totalLength = Seq.of("apple", "banana", "cherry")
    .foldLeft(0, (acc, s) -> acc + s.length());
```

**先看原生能力，再查库——但"window"这个词要拆开看**：`Gatherers.windowFixed / windowSliding`（Java 24，主篇 9.1）覆盖的是**按元素位置切分**的固定/滑动窗口，jOOλ 与之重合的只有这一部分。jOOλ 的 `Seq.window(...)` 重载族提供的是 **SQL 风格分析窗口**：分区（按 `Function` 分区或 `WindowSpecification`）、分区内排序（`Comparator`）、相对当前行的下界/上界范围，以及 `Window` 类型上的 `rank()` / `denseRank()` / `rowNumber()` / `lead()` / `lag()` 与窗口内聚合。这部分 Gatherers 不覆盖，需求落在它上面时，选项仍是 jOOλ、手写自定义 Gatherer，或下推到数据库端的窗口函数。`Seq` 其余的差异化价值是 `zip` / `zipWithIndex` / `foldLeft` / `crossJoin` / 1–16 元组这些 JDK 一直没收编的操作（`groupBy` 的能力 JDK 侧有 `Collectors.groupingBy`，差别只在调用形态——`Seq.groupBy` 直接返回，无需装配 Collector）。0.9.16 发布节奏很慢，但"JDK 不吸收的部分"恰好也是最稳定的部分——作为**小依赖**引入是合理的；不过"零风险"这个词不存在，任何依赖都带着维护、兼容与合规的半径（第 1 节原则二）。

### 3.3 Pragmatica core：面向 Java 25 的 Result / Option / Promise

**衔接点**：主篇 7.3 节手写 `Result` 的"不想引大库"替代品——但要按 2026 年的现状重新介绍。

这个项目需要先澄清脉络：最初的独立仓库 `siy/pragmatica-lite` 已**归档**，开发迁入 [pragmaticalabs/pragmatica](https://github.com/pragmaticalabs/pragmatica) monorepo 的 `core` 模块，Maven 坐标**沿用** `org.pragmatica-lite:core`（原仓库的 core 模块已使用这一 groupId / artifactId；当前 1.0.0-rc4，尚未 GA）。它提供恰好三个核心单子类型，分别瞄准三种要被替代的传统机制：

- **`Result`**——替代检查异常：无异常的错误传播（"Exceptionless Error Propagation"）；
- **`Option`**——替代 null：类型化空安全；
- **`Promise`**——替代 `CompletableFuture` 链：可组合的异步编程。

注意它的 `Result<T>` **只有一个类型参数**：失败通道固定为库自定义的 `Cause` 协议（含 `CoreError` 等 sealed 子类型与 `FatalEscape` 致命错误通道）——它不是任意 `Result<T, E>` 的等价替换，采用它意味着错误类型要适配 Cause 体系，这一点在评估迁移成本时容易被低估。

与 Vavr 的关键差异是"现代 Java 原生主义"：它**面向 Java 25**，直接建立在 sealed interface、record 与模式匹配之上——这恰好与主篇 6.6/7.3 节"用语言原生三件套建模和类型"的路线同构，等于把主篇的手写 `Result` 扩展成带配套组合子的小型库。依赖面也刻意收窄：官方 README 的表述是"最小依赖：仅 slf4j-api 用于日志"（网络资料常简称"零依赖"，以官方表述为准）。

**如实的状态说明**：当前版本是 1.0.0 的候选版，项目年轻、社区生态小，API 仍有调整可能。适合作为观察项，或在团队已整体升级 Java 25、且只需要错误处理单子的场景下试用；保守团队用主篇 7.3 节的手写 `Result` 同样成立。

### 3.4 Cyclops 与 Functional Java：两个特殊角色

这两个库放在一起讲，因为它们都不适合作为新项目的默认选择，但各有一个"值得知道"的理由。

**Cyclops**（AOL/Oath 出品）的 `ReactiveSeq` 曾是"函数式管道 × 响应式执行"交叉点上最有野心的实现：在 Stream 语义之上叠加同步/异步/背压多种执行模式，`AnyM` 抽象试图统一 Vavr、Reactor 等各家的单子 API。现实是 10.4.1（2022 年 5 月）之后再无发布，项目实质休眠。今天若需要响应式管道，直接用 Project Reactor；Cyclops 仅在维护遗留系统时可能遇到。

**Functional Java** 是最接近 Haskell 气质的 Java 库：完整实现了 `IO`、`State`、`Reader` 等经典 Monad 与 `Lens` / `Prism` 等 optics，外加一阶的类型类抽象（`Semigroup` / `Monoid` / `Equal` / `Ord` / `Show`）。需要澄清的一点：它的公开 API **没有**高阶类型（HKT）模拟——"`Kind` / `Higher` 一类的 HKT 抽象"是 HighJ、Cyclops 等库的路线，不在 Functional Java 5.x 的 API 面里。它的最佳用法不是 `pom.xml`，而是**对照阅读**——想知道主篇 2.1 节的 λ 演算 / Monad 概念在 Java 类型系统里"完整映射"会长什么样，读它的源码比读任何教程都直观。5.0 之后同样长期未更新。

---

## 4. 函数式领域建模框架

### 4.1 fmodel-java：Decider 思想的直接实现

**衔接点**：主篇 6.6 节 (b) 的 `Decider<C, S, E>` 正是它的核心抽象——本节补充它的真实现状。

[fraktalio/fmodel-java](https://github.com/fraktalio/fmodel-java) 的定位是 "Functional and Reactive Domain Modeling with Java"（《函数响应式领域建模》思想的 Java 落地）。它的 `domain` 模块完全隔离于应用层、零外部依赖，README 明确说明只用三样东西构建：records、"sealed hierarchy"、switch 穷举模式匹配——与主篇 6.6 节的技术选型完全一致。除了主篇介绍过的三个组件（`decide: (C, S) → List<E>`、`evolve: (S, E) → S`、`initialState`），它还提供了两类值得偷师的能力：

- **组合子**：`combine`（多个 Decider 合并为一个）与 `dimapState`（状态类型映射）——领域组件的"函数组合"落到了聚合层面；
- **Given-When-Then 测试 DSL**：`givenEvents(...).whenCommand(...).thenEvents(...)` 的场景化断言，把"对纯函数聚合做确定性场景测试"（主篇 6.6 节的口径：sealed 类型分支可穷举，输入空间靠重放与性质测试）变成了现成工具。

**如实的状态说明**——README 原文：

> "This project is in experimental phase, and it is not published to Maven Central."

因此它在文章中的正确定位是**概念示范与设计灵感**：想看 Decider 怎么写、怎么测、怎么组合，它是最好的 Java 范本；想在生产中使用这套思想，路径是同作者另三个语言版本 fmodel-kotlin / fmodel-typescript / fmodel-rust（"生产就绪"为项目方自述，本文未独立验证）。配套的 [fmodel-spring-demo](https://github.com/fraktalio/fmodel-spring-demo)（餐厅订单示例，Spring 响应式栈 + R2DBC + PostgreSQL 事件存储）值得注意两点：其一，它用的是 **Kotlin 版 fmodel**——这本身就说明了 Java 侧的成熟度差距；其二，它有一个姊妹仓库 `fmodel-spring-state-stored-demo`，两个项目**共享同一个 domain 包**、只交换 application/adapter 层的组装方式——"同一纯函数领域核心，事件溯源存储 vs 状态存储"的对照实验，比任何文字都更能说明"领域逻辑与持久化策略正交"这句话的含义。

### 4.2 Dewdrop：注解驱动事件溯源的对照组

**衔接点**：与 fmodel 形成"同一问题（事件溯源聚合）的两种风格"对照——主篇 6.6 节说 OOP 聚合把"命令 → 状态 → 事件"折叠进可变过程，Dewdrop 就是这句话的注脚。

[Dewdrop](https://github.com/matsientst/dewdrop) 用注解组织整个事件溯源流程（示例简化自其 README，Apache-2.0）：

```java
@Aggregate
public class AccountAggregate {
    @AggregateId
    UUID accountId;                    // 目前仅支持 UUID 聚合标识

    @CommandHandler
    public AccountCreated handle(CreateAccountCommand command) {
        // 校验后返回事件；框架负责持久化与分发
        return new AccountCreated(command.getAccountId(), command.getName());
    }

    @EventHandler
    public void on(AccountCreated event) {
        this.accountId = event.getAccountId();   // 可变状态，框架重放事件重建
    }
}
```

调用 `dewdrop.executeCommand(command)` 后，框架按 `@AggregateId` 定位事件流、重放历史重建聚合状态、调用命令处理器、再由事件处理器应用状态变更——查询侧另有 `@ReadModel` / `@QueryHandler` 注解体系。对照 fmodel 的写法可以看得非常清楚：**Dewdrop 的 `@CommandHandler` ≈ `decide`，`@EventHandler` ≈ `evolve`**，但它把两个纯函数粘回了可变聚合类的内部，用注解元数据替代显式的数据流。这是"OOP + 事件溯源"的务实组合，代价是领域逻辑与框架生命周期耦合。不过"可变聚合只剩集成测试可写"是过强的推论：不启动容器、直接调用命令处理器并手工重放事件，同样能组织单元级的场景测试——纯度影响的是测试的**便利与确定性**（可变状态需要先搭建），而不是**有无**单元测试的能力。

**如实的状态说明**：官网自述 "Dewdrop is in its early stages of development and is not yet production ready"；后端目前**仅支持 EventStoreDB**；聚合标识仅支持 UUID。生产级 Java 事件溯源需求目前更稳妥的选项仍是 Axon Framework 这类成熟框架——本文收录 Dewdrop 的意义在于风格对照，而非替代推荐。

### 4.3 jOOQ：不是领域建模框架，但是函数式管道的天然数据源

**衔接点**：主篇 4.1 节的"数据源"一节，与 7.1 节"函数式核心，命令式外壳"中外壳的数据加载。

jOOQ 本身是类型安全的 SQL DSL，与函数式领域建模无关；但它与 Stream API 的衔接非常自然，值得在"函数式数据源"的名义下收编：

```java
// fetchStream()：把查询结果作为 Stream 逐行拉取——不先在 jOOQ 层物化整个 Result 对象；
// 但 JDBC 驱动层的行为是另一回事（见代码后的说明）
try (Stream<OrderRecord> orders = ctx
        .selectFrom(ORDER)
        .where(ORDER.STATUS.eq("COMPLETED"))
        .fetchStream()) {                 // Stream 持有游标等 IO 资源，必须 try-with-resources
    List<BigDecimal> totals = orders
        .map(OrderRecord::getAmount)
        .toList();                        // 终止操作仍会物化全部结果，O(n) 空间
}
```

于是主篇 4.1 节的数据源清单可以补全为一个谱系：`Collection.stream()`、`Files.lines()`、`IntStream.range()`、**jOOQ `fetchStream()`**、事件存储的事件流读取——它们都是"命令式外壳加载数据、函数式核心消费数据"的起点。三点边界要说清：其一，`fetchStream()` 返回的 Stream 持有数据库资源，必须显式关闭；其二，"不物化"要分层说——jOOQ 不会先构建完整的 `Result`，但 **JDBC 驱动可能仍然一次性取回全部行**：PostgreSQL 官方文档明确默认全量获取，游标式分批需要正确设置 fetch size、关闭 autocommit 等条件——端到端内存表现由驱动、fetch size、事务配置与终止操作（上例的 `toList()` 就仍会物化全部金额）共同决定；其三，消费这条 Stream 的每一步 `map` 都夹着数据库 I/O——"函数式核心"说的是变换逻辑的纯度，不会因为用了 `map` 就把 I/O 本身变纯。授权方面：jOOQ 按 **edition × 数据库 × 版本支持矩阵**授权（官网有对照表），"商业数据库 dialect 需订阅、开源数据库免费"只是大方向概述。

---

## 5. 完整参考实现

零散的代码片段看十遍，不如一个结构完整的仓库读一遍。以下两个项目分别覆盖本文的两条线：函数式领域建模的 Java 完整落地，与函数式错误处理的跨语言实战。

### 5.1 functional_domain_modeling_in_java：一本书的 Java 25 移植

**衔接点**：主篇 6.6 节（ADT + 纯函数聚合）与 7.1 节（函数式核心，命令式外壳）的**完整可运行对照**。

[horie-t/programming-study](https://github.com/horie-t/programming-study) 仓库中的 [`functional_domain_modeling_in_java`](https://github.com/horie-t/programming-study/tree/master/functional_domain_modeling_in_java) 子项目，技术栈是 Java 25 + Spring Boot 4 + Vavr 0.10.3（`Either` / `Option` / 持久化集合）。它的特别之处在于**有明确的蓝本**：Scott Wlaschin 的《Domain Modeling Made Functional》（原书代码为 F#）——这使它成为"函数式领域建模方法论 → Java 类型系统"的一次忠实转译实验，而非随手写写的 demo。

值得对照主篇阅读的三处结构：

- **状态即类型**：`UnvalidatedOrder → ValidatedOrder → PricedOrder → 事件列` 的状态迁移全部用类型表达（sealed interface + record），与主篇 6.6 节 (a) 的 `OrderState` 建模同构——但注意一处对照落差：该样本的 `PricedOrder` 是**没有构造校验的裸 record**（`new PricedOrder(null, ...)` 在编译层面完全合法），"拿到 `PricedOrder` 就必然已有地址、行项目与总价"并没有自动成立——类型区分的是工作流阶段，"非法状态不可表示"还需要构造校验与值对象约束配合（主篇 6.6 节的紧凑构造器做法正是补齐这一层）；
- **分层与主篇 7.1 的分层表大体对应，纯度需限定**：`domain/`（model 为 ADT 值对象、service 为工作流步骤，**不依赖 Spring**——成立）、`application/`（`PlaceOrderService` 用 `Either` 组合各步骤、port 划分输入输出端口）、`adapter/`（REST 控制器与外部服务的 dummy 实现）——六边形架构（Ports & Adapters）的一个干脆利落的 Java 样本。但"不依赖 Spring"不等于"全部纯函数"：`domain/service/AcknowledgeOrder` 注入并调用了 `application.port.out` 包的发信端口——domain 对 application 存在编译依赖，端口当前是 dummy 实现，换成真实实现即产生副作用。更准确的定位是：一个使用类型化工作流、端口隔离与 `Either` 编排的**学习样本**，领域纯度与依赖方向仍有讨论空间；
- **错误到 HTTP 的完整映射**：`POST /orders` 成功返回事件数组，验证错误 400（`ValidationError`）、定价错误 422、远端服务错误 502——展示了 `Either` 左轨道的领域错误如何穿过应用层最终落到 API 契约上。

一个容易混淆但值得澄清的细节：这个项目移植的是 Wlaschin 的《Domain Modeling Made Functional》（F# 视角，"类型即文档"），而主篇 6.6 节与 fmodel 系列依据的是 Debasish Ghosh 的《Functional and Reactive Domain Modeling》（中译《函数响应式领域建模》，Scala 视角，"分离纯函数与副作用"）——两本书是函数式领域建模的两条主要脉络，一个重类型建模、一个重组件组合，互为补充而非同一本书的两个译名。

### 5.2 resilience-wallet-ledger：Either 修的铁路在真实约束下怎么铺

**衔接点**：主篇 7.3 节 `Result` 管道与 ADT 错误谱系的实战放大版；跨语言（Kotlin）参照。

[fResult/resilience-wallet-ledger](https://github.com/fResult/resilience-wallet-ledger) 是一个 Fintech 账本系统学习项目：Kotlin + Spring Boot WebFlux + Spring Data R2DBC + PostgreSQL，核心实践是 **Railway Oriented Programming（ROP）**——用 Vavr `Either` 让成功轨道与失败轨道在签名上并行可见。README 的"Trapdoor vs Railway"类比与主篇 7.3 节的动机完全一致：异常是看不见的陷阱门，`Either` 让失败路径显式出现在类型签名里（"我可能失败，并且失败形态恰好是这些"）。

比 ROP 本身更有教学价值的是它 README 提出的 **"Commit the Failure" 设计策略**：

> 业务失败（`Either.Left`）**提交**事务——把拒绝事件持久化下来用于审计与 Outbox；只有系统级错误（异常）才触发回滚。

这是"函数式错误处理 × 事务语义"交叉处的真实权衡：失败也是业务事实，丢失失败记录与丢失成功记录同样是数据完整性问题。但**设计意图与当前实现要分开说**（以下为源码阅读结论）：`WalletApplicationService` 的存款 / 取款失败分支目前直接返回 `Either.Left`，分支处留着 "Design Note: Future phase - Save `DepositFailed` event here" 的注释——失败事件类型（`MoneyDepositFailed` 等）已在 domain 层定义但尚未接线，Outbox 的领域事件转换也只处理三种成功事件；同时，持久化边界通过 `commandToEither` / `queryToEither` 扩展用 `onErrorResume` 把异常转换为 `Either.Left` 值（兜底分支把任意未知系统异常也映射为 `Unexpected System Error` 值）——需要触发回滚的错误信号可能就此变成"正常完成的失败结果"，事务拦截器未必再看得见异常。因此本节的正确定位是**值得研究、但实现尚需审查的事务设计案例**；要在自己的系统落地这类策略，四条必要条件先核一遍：拒绝路径不留部分资金变更；拒绝记录与 Outbox 的事务边界明确；需要回滚的错误不被误转为正常结果；用事务集成测试验证提交、回滚与取消行为。（以上为源码静态阅读的风险提示，未做数据库事务实验复现。）项目按五阶段路线图演进（弹性模式：幂等、熔断、链路追踪等在后续阶段），作者另有一组配套文章（"The Contract of Failure" 系列）。

**如实的状态说明**：作者明确定位为个人沙盒与参考实现（"Educational Purpose Only"，不接受 PR、不提供支持），且路线图尚在进行中——把它当作"高质量博客文章的可运行版"来读，而非脚手架来抄。

---

## 6. 选型决策：按场景而非按流行度

把前文收拢为一张按问题驱动的决策表。多数场景下"第一站"都是主篇的手写方案——它应该始终是可行的退路（原则见第 1 节）。

| 你要解决的问题 | 第一站 | 引入库时 | 完整参考 |
|----------------|--------|----------|----------|
| 领域错误的类型化传播 | 主篇 7.3 手写 `Result` + ADT 错误谱系 | Vavr `Either`（快速失败）/ `Validation`（错误累积）；Java 25 团队可评估 Pragmatica core | 5.2（ROP 实战） |
| 把抛异常的第三方调用纳入管道 | 主篇 5.3 的 try-catch 包装 | Vavr `Try`（按异常类型恢复） | — |
| Stream 缺 zip / foldLeft / 元组 | foldLeft 可用手写循环覆盖（主篇 6.6、7.2：3 参数 reduce 受 combiner 契约限制）；zip / 元组无简洁手写等价 | jOOλ | — |
| 固定/滑动切窗 / scan / 自定义中间操作 | **原生 `Gatherers`（Java 24+，主篇 9.1）** | — | — |
| SQL 风格窗口（分区 / 排名 / lead / lag / 窗口聚合） | Gatherers 不覆盖（3.2 节） | jOOλ `window` 体系；或手写自定义 Gatherer | 数据库端窗口函数（下推） |
| 不可变集合的结构共享修改 | `List.of()` + 流式复制 | Vavr 持久化集合 | — |
| 聚合的事件溯源建模（学习） | 主篇 6.6 手写 `Decider` | —（本文考察的 Java 版 fmodel 仍为实验项目） | 4.1 fmodel-java + fmodel-spring-demo |
| 聚合的事件溯源建模（生产，Java） | — | Axon 等成熟框架（注解驱动 OOP 风格，与 4.2 节 Dewdrop 同路） | 4.2 Dewdrop（对照） |
| 数据库查询接入函数式管道 | 集合 `stream()`；JPA `getResultStream()`（游标行为随 JPA provider 与驱动配置而异，与集合流不天然等价） | jOOQ `fetchStream()`（注意资源关闭与 4.3 节的分层语义） | — |
| "函数式核心，命令式外壳"完整分层 | 主篇 7.1 分层表 | — | 5.1（Java 样本）、4.1（Kotlin 版 fmodel demo） |
| 理解 Monad 在 Java 中的完整形态 | 主篇 2.1 | —（不建议引入） | 3.4 Functional Java（源码阅读） |

三条经验法则收尾：

1. **错误处理类型在手写与复用之间按成本对称评估**——手写 `Result` 与领域绑定深（ADT 错误谱系必须自己定义），但也意味着自己长期维护 null / 异常契约、泛型组合子、错误映射与异步 / 事务的衔接；库方案把这笔成本摊给了维护方。类型面小、边界清晰的场景手写划算；组合面大、或团队已有 Vavr 经验时，复用成熟实现往往更便宜——不是"必须手写"。
2. **每个"库能做"的条目都先查一遍 JDK 版本对应表**（主篇第 9 章）——函数式库的能力边界正在被语言原生能力逐块蚕食，这是 Java 生态的特殊现象，也是"轻量优先"的现实依据。
3. **experimental 项目的风险预算按用途分层**——概念学习与隔离的技术验证几乎零风险，内部工具次之，生产关键路径才需要"读而不引"。fmodel-java 在前三类场景都可以上手；把它的 Decider 组合子与测试 DSL 的设计带走、回到自己的 sealed + record 世界里手写，则是其中风险最低的用法。

---

## 7. 时效性说明与状态快照

本文所有版本号与维护状态核查于 **2026 年 11 月**，来源为各项目官方渠道（GitHub 仓库 / 官网 / Maven Central 记录）；条目括注的"依据"标明验证级别——README 自述、源码阅读或 Maven Central 机器记录，未标注"编译 / 运行"的均未做工程级验证。函数式 Java 生态变化不快，但"库的兴衰"恰恰是这个生态的常态（见 Cyclops、Functional Java），引用前请复核：

- **Vavr**：0.11.0（2025-12）→ 1.0.0 / 1.0.1 → **1.1.0**（Maven Central 元数据，2026-10-24 上传）；1.x 线为 0.11.0 的 drop-in 替代、保持 Java 8，现代化规划在 2.x 线（依据：GitHub Releases 发布说明 + Maven Central 元数据；本文示例在 0.11.0 验证，未在 1.x 线重验）；
- **jOOλ 0.9.16**：最新版本；主 artifact 构建目标 Java 11，Java 8 环境用 `org.jooq:jool-java-8` 同版本（依据：Maven Central pom 与 artifact 记录）；发布节奏一贯很慢（0.9.13 为 2019 年），API 稳定；
- **jOOQ**：3.21 线持续更新（已至 3.21.10，Maven Central）；3.20（2025-02）起开源线要求 Java 21，商业版另有 Java 17 / 11 发行物（依据：官方 JDK 支持矩阵）；
- **Pragmatica core**：`org.pragmatica-lite:core:1.0.0-rc4`；原 `siy/pragmatica-lite` 仓库已归档、迁入 `pragmaticalabs/pragmatica` monorepo；官方 README 表述依赖为"仅 slf4j-api"；
- **Cyclops 10.4.1**：2022 年 5 月，此后 Maven Central 无新版本；
- **Functional Java 5.0**：最后发布活动约 2022 年初；
- **fmodel-java**：README 自述 "in experimental phase, not published to Maven Central"；构建目标 Java 23（依据：仓库 pom.xml 的 source/target=23——所用语言特性 Java 21 已足够，但不能据此用 Java 21 原样构建）；"生产就绪"版本 fmodel-kotlin / -typescript / -rust 为项目方自述；
- **Dewdrop**：官网自述 "in its early stages of development and is not yet production ready"；后端仅支持 EventStoreDB，聚合标识仅支持 UUID；
- **functional_domain_modeling_in_java**：Java 25 / Spring Boot 4 / Vavr 0.10.3，个人学习仓库（书籍移植项目）；
- **resilience-wallet-ledger**：Kotlin / Spring Boot WebFlux / R2DBC / PostgreSQL；作者声明教育用途、不接受 PR。

---

*本文为《Java 函数式编程深度解析》的姊妹篇，两者独立成文、交叉引用。文中项目状态以第 7 节核查日期为准；引入任何第三方依赖前，请结合团队 JDK 版本、维护风险与合规要求自行评估。*
