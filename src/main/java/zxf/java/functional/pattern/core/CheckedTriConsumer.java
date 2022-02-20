package zxf.java.functional.pattern.core;

public interface CheckedTriConsumer<T, U, P> {
    void accept(T t, U u, P p) throws Exception;
}