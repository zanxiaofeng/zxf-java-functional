package zxf.java.functional.core;

import zxf.java.functional.core.function.*;


import java.util.function.*;

public class Currying {
    public static <T, U> CheckedConsumer<U> curryingConsumer(T t, CheckedBiConsumer<T, U> consumer) {
        return (u) -> consumer.accept(t, u);
    }

    public static <T, U, P> CheckedBiConsumer<U, P> curryingConsumer(T t, CheckedTriConsumer<T, U, P> consumer) {
        return (u, p) -> consumer.accept(t, u, p);
    }

    public static <T, U, P> CheckedConsumer<P> curryingConsumer(T t, U u, CheckedTriConsumer<T, U, P> consumer) {
        return (p) -> consumer.accept(t, u, p);
    }

    public static <T, U, R> CheckedFunction<U, R> curryingFunction(T t, CheckedBiFunction<T, U, R> function) {
        return (u) -> {
            return function.apply(t, u);
        };
    }

    public static <T, U, P, R> CheckedBiFunction<U, P, R> curryingFunction(T t, CheckedTriFunction<T, U, P, R> function) {
        return (u, p) -> {
            return function.apply(t, u, p);
        };
    }

    public static <T, U, P, R> CheckedFunction<P, R> curryingFunction(T t, U u, CheckedTriFunction<T, U, P, R> function) {
        return (p) -> {
            return function.apply(t, u, p);
        };
    }

    public static <T, U> Predicate<U> curryingPredicate(T t, BiPredicate<T, U> predicate) {
        return (u) -> {
            return predicate.test(t, u);
        };
    }

    public static <T, U, P> BiPredicate<U, P> curryingPredicate(T t, TriPredicate<T, U, P> predicate) {
        return (u, p) -> {
            return predicate.test(t, u, p);
        };
    }

    public static <T, U, P> Predicate<P> curryingPredicate(T t, U u, TriPredicate<T, U, P> predicate) {
        return (p) -> {
            return predicate.test(t, u, p);
        };
    }
}
