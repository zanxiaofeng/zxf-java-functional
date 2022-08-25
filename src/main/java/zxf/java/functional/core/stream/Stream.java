package zxf.java.functional.core.stream;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Stream<T> {
    private Processor<?, T> finalProcessor;

    public Stream(Processor<?, T> initialProcessor) {
        this.finalProcessor = initialProcessor;
    }

    public Stream<T> filter(Predicate<T> predicate) {
        this.finalProcessor = new FilterProcessor(this.finalProcessor, predicate);
        return this;
    }

    public <R> Stream<R> map(Function<T, R> mapper) {
        return new Stream<R>(new MapProcessor<>(finalProcessor, mapper));
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

    public static <T> Stream<T> list(List<T> source) {
        return new Stream<>(new InitialProcessor<>(source));
    }
}
