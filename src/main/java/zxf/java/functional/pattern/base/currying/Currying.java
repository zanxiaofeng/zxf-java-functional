package zxf.java.functional.pattern.base.currying;

import zxf.java.functional.pattern.base.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Currying {
    public static <T, U, R> Function<U, R> currying(T x, BiFunction<T, U, R> function) {
        return (y) -> {
            return function.apply(x, y);
        };
    }

    public static <T, U, P, R> BiFunction<U, P, R> currying(T x, TriFunction<T, U, P, R> function) {
        return (y, z) -> {
            return function.apply(x, y, z);
        };
    }

    public static <T, U, P, R> Function<P, R> currying(T x, U y, TriFunction<T, U, P, R> function) {
        return (z) -> {
            return function.apply(x, y, z);
        };
    }
}
