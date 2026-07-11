package zxf.java.functional.core.stream;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 自实现的惰性 Stream，对应 Readme 中「流式 API / 责任链 + 终端操作」概念。
 *
 * <p>构造期只组装 {@link Processor} 责任链（filter/map/flatMap 都返回新的 Stream
 * 或追加处理器），不真正计算；直到调用终端操作（collect / reduce / count /
 * forEach）才沿责任链拉取数据。</p>
 */
public class Stream<T> {
    /** 流水线最末端（最后包装）的处理器，终端操作从这里拉取元素。 */
    private Processor<?, T> finalProcessor;

    public Stream(Processor<?, T> initialProcessor) {
        this.finalProcessor = initialProcessor;
    }

    /**
     * 包级可见：返回当前流水线的底层处理器，供 {@link FlatMapProcessor}
     * 拉取子流元素使用。仅限 core.stream 包内部调用。
     */
    Processor<?, T> internalProcessor() {
        return finalProcessor;
    }

    public Stream<T> filter(Predicate<T> predicate) {
        this.finalProcessor = new FilterProcessor(this.finalProcessor, predicate);
        return this;
    }

    public <R> Stream<R> map(Function<T, R> mapper) {
        return new Stream<R>(new MapProcessor<>(finalProcessor, mapper));
    }

    /**
     * flatMap：对应 Monad 的 bind/flatten 操作。
     * 把每个 T 映射成一条 {@code Stream<R>}，再拍平成一条流。
     */
    public <R> Stream<R> flatMap(Function<T, Stream<R>> mapper) {
        return new Stream<R>(new FlatMapProcessor<>(finalProcessor, mapper));
    }

    public <A, R> R collect(Collector<T, A, R> collector) {
        A results = collector.supplier().get();

        try {
            while (true) {
                collector.accumulator().accept(results, finalProcessor.next());
            }
        } catch (EndIteratorException e) {
            return collector.finisher().apply(results);
        }
    }

    /**
     * 归约（带初始值）。对应 Readme 中 reduce 的最常用形式。
     *
     * <p>JDK Stream 共有 3 种 reduce 重载，这里实现第 1 种；其语义为：
     * {@code acc = identity; for each t: acc = reducer.apply(acc, t); return acc;}。</p>
     *
     * <ul>
     *   <li>重载1（本方法）：{@code T reduce(T identity, BinaryOperator<T> reducer)}——
     *       有初始值，结果必不为空，常用于求和/求积/求最大值。</li>
     *   <li>重载2（见 {@link #reduce(BinaryOperator)}）：无初始值，返回
     *       {@code java.util.Optional<T>}，空流返回 empty。</li>
     *   <li>重载3（本实现暂未提供）：{@code <U> U reduce(U identity,
     *       BiFunction<U,T,U> reducer, BinaryOperator<U> combiner)}——
     *       类型不同的归约，combiner 仅在并行流中有意义，本实现是顺序流故略。</li>
     * </ul>
     */
    public T reduce(T identity, BinaryOperator<T> reducer) {
        Objects.requireNonNull(reducer, "reducer 不能为空");
        T acc = identity;
        try {
            while (true) {
                acc = reducer.apply(acc, finalProcessor.next());
            }
        } catch (EndIteratorException e) {
            return acc;
        }
    }

    /**
     * 归约（不带初始值）。对应 Readme 中 reduce 的第 2 种形式。
     *
     * <p>语义：取第一个元素作为初始累加值，依次与后续元素两两归约；
     * 空流返回 {@link java.util.Optional#empty()}。</p>
     */
    public java.util.Optional<T> reduce(BinaryOperator<T> reducer) {
        Objects.requireNonNull(reducer, "reducer 不能为空");
        boolean foundAny = false;
        T acc = null;
        try {
            while (true) {
                T next = finalProcessor.next();
                if (!foundAny) {
                    acc = next;
                    foundAny = true;
                } else {
                    acc = reducer.apply(acc, next);
                }
            }
        } catch (EndIteratorException e) {
            return foundAny ? java.util.Optional.ofNullable(acc) : java.util.Optional.empty();
        }
    }

    /** 元素个数。对应 Readme 中 count 终端操作。 */
    public long count() {
        long count = 0;
        try {
            while (true) {
                finalProcessor.next();
                count++;
            }
        } catch (EndIteratorException e) {
            return count;
        }
    }

    /** 内部迭代消费每个元素。对应 Readme 中 forEach 终端操作。 */
    public void forEach(Consumer<T> action) {
        Objects.requireNonNull(action, "action 不能为空");
        try {
            while (true) {
                action.accept(finalProcessor.next());
            }
        } catch (EndIteratorException e) {
            // 迭代正常结束
        }
    }

    public static <T> Stream<T> list(List<T> source) {
        return new Stream<>(new InitialProcessor<>(source));
    }
}
