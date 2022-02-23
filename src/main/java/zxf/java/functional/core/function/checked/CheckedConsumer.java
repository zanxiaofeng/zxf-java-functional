package zxf.java.functional.core.function.checked;

@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T t) throws Exception;
}