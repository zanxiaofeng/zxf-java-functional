package zxf.java.functional.pattern.base.currying;

import zxf.java.functional.pattern.base.TriFunction;
import zxf.java.functional.pattern.base.TriPredicate;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static <T, U> Predicate<U> currying(T t, BiPredicate<T, U> predicate) {
        return (u) -> {
            return predicate.test(t, u);
        };
    }

    public static <T, U, P> BiPredicate<U, P> currying(T t, TriPredicate<T, U, P> predicate) {
        return (u, p) -> {
            return predicate.test(t, u, p);
        };
    }

    public static <T, U, P> Predicate<P> currying(T t, U u, TriPredicate<T, U, P> predicate) {
        return (p) -> {
            return predicate.test(t, u, p);
        };
    }
}
