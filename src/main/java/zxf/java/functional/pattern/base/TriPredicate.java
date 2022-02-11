package zxf.java.functional.pattern.base;

public interface TriPredicate<T, U, P> {
    boolean test(T t, U u, P p);
}
