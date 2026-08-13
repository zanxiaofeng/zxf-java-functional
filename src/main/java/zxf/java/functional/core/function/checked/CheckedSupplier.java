package zxf.java.functional.core.function.checked;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}
