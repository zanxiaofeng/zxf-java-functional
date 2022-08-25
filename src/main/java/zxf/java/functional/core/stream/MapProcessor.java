package zxf.java.functional.core.stream;

import java.util.function.Function;

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
        P prevNext = null;
        while (prevNext == null) {
            prevNext = prevNext();
        }
        return mapper.apply(prevNext);
    }
}
