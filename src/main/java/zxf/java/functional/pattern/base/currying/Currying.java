package zxf.java.functional.pattern.base.currying;

import zxf.java.functional.pattern.base.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Currying {
    public static <T, U, R> Function<U, R> currying(T t, BiFunction<T, U, R> function) {
        return (u) -> {
            return function.apply(t, u);
        };
    }

    public static <T, U, P, R> BiFunction<U, P, R> currying(T t, TriFunction<T, U, P, R> function) {
        return (u, p) -> {
            return function.apply(t, u, p);
        };
    }

    public static <T, U, P, R> Function<P, R> currying(T t, U u, TriFunction<T, U, P, R> function) {
        return (p) -> {
            return function.apply(t, u, p);
        };
    }
}
