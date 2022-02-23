package zxf.java.functional.core.function.checked;


@FunctionalInterface
public interface CheckedBiFunction<T, U, R> {
    R apply(T t, U u) throws Exception;
}
