package zxf.java.functional.core.stream;

import java.util.function.Function;

/**
 * map 算子处理器：对上游每一个元素应用 mapper，产出下游元素。
 *
 * <p>核心修正：不再用 {@code null} 作为「未取到值」的哨兵。
 * 旧实现 {@code while (prevNext == null)} 会在上游元素本身为 null、
 * 或 mapper 返回 null 时陷入死循环。正确做法是依赖
 * {@link EndIteratorException} 作为迭代终止的唯一信号——
 * {@link #prevNext()} 在上游耗尽时抛出该异常，直接向上传播即可，
 * 这样 null 就能像普通元素一样流过流水线。</p>
 */
public class MapProcessor<P, T> implements Processor<P, T> {
    private Processor<?, P> prevProcess;
    private Function<P, T> mapper;

    public MapProcessor(Processor<?, P> prevProcess, Function<P, T> mapper) {
        this.prevProcess = prevProcess;
        this.mapper = mapper;
    }

    @Override
    public Processor<?, P> getPrevProcess() {
        return prevProcess;
    }

    @Override
    public T next() throws EndIteratorException {
        // prevNext() 在上游耗尽时抛出 EndIteratorException，作为终止信号向上传播。
        // 这里不再循环跳过 null，因此 null 元素 / mapper 返回 null 都能正确通过。
        P prevNext = prevNext();
        return mapper.apply(prevNext);
    }
}
