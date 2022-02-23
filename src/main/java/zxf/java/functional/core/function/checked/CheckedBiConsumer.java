package zxf.java.functional.core.function.checked;

@FunctionalInterface
public interface CheckedBiConsumer<T, U> {
    void accept(T t, U u) throws Exception;
}