package zxf.java.functional.core.stream;

public interface Processor<P, T> {
    Processor<?, P> getPrevProcess();

    T next() throws EndIteratorException;

    default P prevNext() throws EndIteratorException {
        return getPrevProcess().next();
    }
}
