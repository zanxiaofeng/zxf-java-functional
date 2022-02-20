package zxf.java.functional.core.function;

@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T t) throws Exception;
}