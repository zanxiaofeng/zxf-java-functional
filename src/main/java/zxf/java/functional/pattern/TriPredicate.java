package zxf.java.functional.pattern;

public interface TriPredicate<T, U, P> {
    boolean test(T t, U u, P p);
}
