package zxf.java.functional.core.stream;

import java.util.function.Predicate;

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
        T prevNext = null;
        do {
            prevNext = null;
            while (prevNext == null) {
                prevNext = prevNext();
            }
        } while (!predicate.test(prevNext));
        return prevNext;
    }
}
