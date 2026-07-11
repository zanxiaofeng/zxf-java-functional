package zxf.java.functional.core.stream;

import java.util.function.Function;

/**
 * flatMap 算子处理器：把上游每个元素展开成一条子流，再依次「拍平」输出。
 *
 * <p>对应 Readme 中 Monad 的 flatten 语义：{@code Stream<T>} 经
 * {@code Function<T, Stream<R>>} 映射后再压平，得到 {@code Stream<R>}。</p>
 *
 * <p>实现要点：维护一个「当前子流处理器」{@code currentInner}。
 * 当子流耗尽（抛 {@link EndIteratorException}）时，从上游拉取下一个元素，
 * 构造新的子流处理器继续迭代；当上游也耗尽时，异常自然向上传播终止整条流水线。</p>
 *
 * @param <P> 上游元素类型
 * @param <R> 下游元素类型（子流元素类型）
 */
public class FlatMapProcessor<P, R> implements Processor<P, R> {
    private Processor<?, P> prevProcessor;
    private Function<P, Stream<R>> mapper;
    /** 当前正在迭代的子流处理器；null 表示需要从上游拉取下一个元素构造新子流。 */
    private Processor<?, R> currentInner = null;

    public FlatMapProcessor(Processor<?, P> prevProcessor, Function<P, Stream<R>> mapper) {
        this.prevProcessor = prevProcessor;
        this.mapper = mapper;
    }

    @Override
    public Processor<?, P> getPrevProcess() {
        return prevProcessor;
    }

    @Override
    public R next() throws EndIteratorException {
        while (true) {
            if (currentInner == null) {
                // 上游耗尽时 prevNext() 抛 EndIteratorException 终止；不再用 null 做哨兵。
                P element = prevNext();
                Stream<R> innerStream = mapper.apply(element);
                currentInner = innerStream.internalProcessor();
            }
            try {
                return currentInner.next();
            } catch (EndIteratorException e) {
                // 当前子流耗尽，切换到上游下一个元素
                currentInner = null;
            }
        }
    }
}
