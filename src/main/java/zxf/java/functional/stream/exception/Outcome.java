package zxf.java.functional.stream.exception;

import zxf.java.functional.core.function.checked.CheckedFunction;
import zxf.java.functional.core.function.checked.CheckedSupplier;

import java.util.function.Function;

/**
 * Outcome — 把「异常」从控制流的中断信号变成「数据流中的值」。
 *
 * <p>核心思想（Either / Try Monad）：用一个类型包装「成功值」或「失败原因」，
 * 让异常作为普通值在 Stream 管道中流动。这样单个元素的失败不会中断整个流，
 * 处理结束后还能分离成功结果与失败记录。</p>
 *
 * <p><b>关键设计：{@link #map(Function)} 内部 catch 异常。</b>
 * 这样在 {@code outcome.map(step2).map(step3)} 链中，任何一步的异常都会被
 * 自动捕获为 {@link Failure}，不会中断 Stream。</p>
 *
 * <p>使用示例（Stream 三步 map 链，每步都可能抛异常）：
 * <pre>{@code
 * list.stream()
 *     .map(Outcome.lift(step1))            // String  -> Outcome<Integer>
 *     .map(outcome -> outcome.map(step2))  // Outcome<Integer> -> Outcome<Double>
 *     .map(outcome -> outcome.map(step3))  // Outcome<Double>  -> Outcome<String>
 *     .collect(Collectors.partitioningBy(Outcome::isSuccess));
 * }</pre>
 *
 * @param <T> 成功时携带的值类型
 */
public sealed interface Outcome<T> permits Outcome.Success, Outcome.Failure {

    // ==================== 工厂方法 ====================

    /**
     * 把可能抛异常的操作包装成 Outcome。
     *
     * @param supplier 可能抛异常的操作
     * @return 成功则 {@link Success}，异常则 {@link Failure}
     */
    static <T> Outcome<T> of(CheckedSupplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    /**
     * 把可能抛异常的 {@link CheckedFunction} 提升为 {@code Function<T, Outcome<R>>}，
     * 用于 Stream 管道的<b>第一步</b> map —— 将原始元素 lift 进 Outcome 上下文。
     *
     * @param function 可能抛异常的转换函数
     * @return 接受 T、返回 Outcome&lt;R&gt; 的标准 Function（可直接传入 Stream.map）
     */
    static <T, R> Function<T, Outcome<R>> lift(CheckedFunction<T, R> function) {
        return t -> of(() -> function.apply(t));
    }

    // ==================== 核心 monad 操作 ====================

    /**
     * 对成功值应用转换函数；若转换函数抛异常则返回 {@link Failure}。
     *
     * <p><b>关键：</b>map 内部 catch 异常，这是实现「异常变值」的核心机制。
     * 已是 {@link Failure} 的 Outcome 直接短路返回，不再执行后续 map。</p>
     *
     * @param mapper 转换函数（若抛异常会被捕获为 Failure）
     * @param <R>    目标类型
     * @return 新的 Outcome
     */
    <R> Outcome<R> map(Function<? super T, ? extends R> mapper);

    /**
     * 对成功值应用返回 Outcome 的转换函数（monad bind）。
     * 用于链式组合多个自身就返回 Outcome 的操作。
     *
     * @param mapper 返回 Outcome 的转换函数
     * @param <R>    目标类型
     * @return 新的 Outcome
     */
    <R> Outcome<R> flatMap(Function<? super T, Outcome<R>> mapper);

    // ==================== 查询 / 提取 ====================

    /** 是否成功 */
    boolean isSuccess();

    /** 获取成功值，失败时返回 null */
    T getOrNull();

    /** 获取失败原因，成功时返回 null */
    Exception getCauseOrNull();

    /** 获取成功值，失败时返回指定的默认值 */
    T getOrElse(T defaultValue);

    /**
     * 模式匹配提取：成功走 {@code onSuccess}，失败走 {@code onFailure}。
     *
     * @param onSuccess  成功时的映射函数
     * @param onFailure  失败时的映射函数（接收异常）
     * @param <R>        结果类型
     * @return 两个分支之一的执行结果
     */
    <R> R fold(Function<? super T, ? extends R> onSuccess,
               Function<? super Exception, ? extends R> onFailure);

    // ==================== 成功 ====================

    record Success<T>(T value) implements Outcome<T> {

        @Override
        public <R> Outcome<R> map(Function<? super T, ? extends R> mapper) {
            try {
                return new Success<>(mapper.apply(value));
            } catch (Exception e) {
                return new Failure<>(e);
            }
        }

        @Override
        public <R> Outcome<R> flatMap(Function<? super T, Outcome<R>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public T getOrNull() {
            return value;
        }

        @Override
        public Exception getCauseOrNull() {
            return null;
        }

        @Override
        public T getOrElse(T defaultValue) {
            return value;
        }

        @Override
        public <R> R fold(Function<? super T, ? extends R> onSuccess,
                          Function<? super Exception, ? extends R> onFailure) {
            return onSuccess.apply(value);
        }

        @Override
        public String toString() {
            return "Success(" + value + ")";
        }
    }

    // ==================== 失败 ====================

    record Failure<T>(Exception cause) implements Outcome<T> {

        @Override
        public <R> Outcome<R> map(Function<? super T, ? extends R> mapper) {
            // 短路：失败值跳过后续所有 map
            return new Failure<>(cause);
        }

        @Override
        public <R> Outcome<R> flatMap(Function<? super T, Outcome<R>> mapper) {
            return new Failure<>(cause);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public T getOrNull() {
            return null;
        }

        @Override
        public Exception getCauseOrNull() {
            return cause;
        }

        @Override
        public T getOrElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public <R> R fold(Function<? super T, ? extends R> onSuccess,
                          Function<? super Exception, ? extends R> onFailure) {
            return onFailure.apply(cause);
        }

        @Override
        public String toString() {
            return "Failure(" + cause.getClass().getSimpleName() + ": " + cause.getMessage() + ")";
        }
    }
}
