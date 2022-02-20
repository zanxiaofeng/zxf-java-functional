package zxf.java.functional.pattern.core;

public interface CheckedBiConsumer<T, U> {
    void accept(T t, U u) throws Exception;
}