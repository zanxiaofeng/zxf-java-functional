package zxf.java.functional.core.function;

@FunctionalInterface
public interface CheckedTriConsumer<T, U, P> {
    void accept(T t, U u, P p) throws Exception;
}