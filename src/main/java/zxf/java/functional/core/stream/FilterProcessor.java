package zxf.java.functional.core.stream;

import java.util.function.Predicate;

/**
 * filter 算子处理器：对上游元素逐个测试 predicate，只放行匹配的元素。
 *
 * <p>核心修正：旧实现 {@code while (prevNext == null)} 用 null 当哨兵，
 * 一旦上游存在合法 null 元素就会死循环。修正后直接循环取上游元素，
 * 由 {@link #prevNext()} 在耗尽时抛 {@link EndIteratorException} 终止，
 * null 元素与其它元素一样可被 predicate 测试。</p>
 */
public class FilterProcessor<T> implements Processor<T, T> {
    private Processor<?, T> prevProcessor;
    private Predicate<T> predicate;

    public FilterProcessor(Processor<?, T> prevProcessor, Predicate<T> predicate) {
        this.prevProcessor = prevProcessor;
        this.predicate = predicate;
    }

    @Override
    public Processor<?, T> getPrevProcess() {
        return prevProcessor;
    }

    @Override
    public T next() throws EndIteratorException {
        // 持续从上游拉取元素直到命中谓词；上游耗尽时 prevNext() 抛 EndIteratorException 自然终止。
        while (true) {
            T prevNext = prevNext();
            if (predicate.test(prevNext)) {
                return prevNext;
            }
        }
    }
}
