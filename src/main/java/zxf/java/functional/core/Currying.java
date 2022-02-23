package zxf.java.functional.core;

import zxf.java.functional.core.function.*;
import zxf.java.functional.core.function.checked.*;

import java.util.function.*;

public class Currying {
    //Common
    public static <T, U> Function<T, CheckedConsumer<U>> curryingConsumer(CheckedBiConsumer<T, U> consumer) {
        return (t) -> {
            return (u) -> {
                consumer.accept(t, u);
            };
        };
    }

    public static <T, U> CheckedConsumer<U> curryingConsumer(T t, CheckedBiConsumer<T, U> consumer) {
        return curryingConsumer(consumer).apply(t);
    }

    //Common
    public static <T, U, P> Function<T, Function<U, CheckedConsumer<P>>> curryingConsumer(CheckedTriConsumer<T, U, P> consumer) {
        return (t) -> {
            return (u) -> {
                return (p) -> {
                    consumer.accept(t, u, p);
                };
            };
        };
    }

    public static <T, U, P> CheckedBiConsumer<U, P> curryingConsumer(T t, CheckedTriConsumer<T, U, P> consumer) {
        return (u, p) -> consumer.accept(t, u, p);
    }

    public static <T, U, P> CheckedConsumer<P> curryingConsumer(T t, U u, CheckedTriConsumer<T, U, P> consumer) {
        return curryingConsumer(consumer).apply(t).apply(u);
    }

    //Common
    public static <T, U, R> Function<T, CheckedFunction<U, R>> curryingFunction(CheckedBiFunction<T, U, R> function) {
        return (t) -> {
            return (u) -> {
                return function.apply(t, u);
            };
        };
    }

    public static <T, U, R> CheckedFunction<U, R> curryingFunction(T t, CheckedBiFunction<T, U, R> function) {
        return curryingFunction(function).apply(t);
    }

    //Common
    public static <T, U, P, R> Function<T, Function<U, CheckedFunction<P, R>>> curryingFunction(CheckedTriFunction<T, U, P, R> function) {
        return (t) -> {
            return (u) -> {
                return (p) -> {
                    return function.apply(t, u, p);
                };
            };
        };
    }

    public static <T, U, P, R> CheckedBiFunction<U, P, R> curryingFunction(T t, CheckedTriFunction<T, U, P, R> function) {
        return (u, p) -> function.apply(t, u, p);
    }

    public static <T, U, P, R> CheckedFunction<P, R> curryingFunction(T t, U u, CheckedTriFunction<T, U, P, R> function) {
        return curryingFunction(function).apply(t).apply(u);
    }

    //Common
    public static <T, U> Function<T, Predicate<U>> curryingPredicate(BiPredicate<T, U> predicate) {
        return (t) -> {
            return (u) -> {
                return predicate.test(t, u);
            };
        };
    }

    public static <T, U> Predicate<U> curryingPredicate(T t, BiPredicate<T, U> predicate) {
        return curryingPredicate(predicate).apply(t);
    }

    //Common
    public static <T, U, P> Function<T, Function<U, Predicate<P>>> curryingPredicate(TriPredicate<T, U, P> predicate) {
        return (t) -> {
            return (u) -> {
                return (p) -> {
                    return predicate.test(t, u, p);
                };
            };
        };
    }

    public static <T, U, P> BiPredicate<U, P> curryingPredicate(T t, TriPredicate<T, U, P> predicate) {
        return (u, p) -> {
            return predicate.test(t, u, p);
        };
    }

    public static <T, U, P> Predicate<P> curryingPredicate(T t, U u, TriPredicate<T, U, P> predicate) {
        return curryingPredicate(predicate).apply(t).apply(u);
    }
}
