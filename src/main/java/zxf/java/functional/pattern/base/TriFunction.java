package zxf.java.functional.pattern.base;

public interface TriFunction<T, U, P, R> {
    R apply(T x, U y, P z);
}
