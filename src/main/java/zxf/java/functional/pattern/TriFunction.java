package zxf.java.functional.pattern;

public interface TriFunction<T, U, P, R> {
    R apply(T x, U y, P z);
}
