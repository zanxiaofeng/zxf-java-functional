package zxf.java.functional.core.stream;

import java.util.Iterator;
import java.util.List;

public class InitialProcessor<T> implements Processor<T, T> {
    private Iterator<T> sourceIterator;

    public InitialProcessor(List<T> source) {
        sourceIterator = source.listIterator();
    }

    @Override
    public Processor<?, T> getPrevProcess() {
        return null;
    }

    @Override
    public T next() throws EndIteratorException {
        if (sourceIterator.hasNext()) {
            return sourceIterator.next();
        }
        throw new EndIteratorException();
    }
}
