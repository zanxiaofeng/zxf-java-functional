package zxf.java.functional.pattern.core;

public interface CheckedConsumer<T> {
    void accept(T t) throws Exception;
}