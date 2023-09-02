package zxf.java.functional.core;

import zxf.java.functional.core.function.*;
import zxf.java.functional.core.function.checked.*;

import java.util.function.*;

public class Currying {
    public static <T, U> Function<T, CheckedConsumer<U>> curryingConsumer(CheckedBiConsumer<T, U> consumer) {
        return (t) -> {
            return (u) -> {
                consumer.accept(t, u);
            };
        };
    }

    public static <T, U, P> Function<T, Function<U, CheckedConsumer<P>>> curryingConsumer(CheckedTriConsumer<T, U, P> consumer) {
        return (t) -> {
            return (u) -> {
                return (p) -> {
                    consumer.accept(t, u, p);
                };
            };
        };
    }

    public static <T, U, R> Function<T, CheckedFunction<U, R>> curryingFunction(CheckedBiFunction<T, U, R> function) {
        return (t) -> {
            return (u) -> {
                return function.apply(t, u);
            };
        };
    }

    public static <T, U, P, R> Function<T, Function<U, CheckedFunction<P, R>>> curryingFunction(CheckedTriFunction<T, U, P, R> function) {
        return (t) -> {
            return (u) -> {
                return (p) -> {
                    return function.apply(t, u, p);
                };
            };
        };
    }

    public static <T, U> Function<T, Predicate<U>> curryingPredicate(BiPredicate<T, U> predicate) {
        return (t) -> {
            return (u) -> {
                return predicate.test(t, u);
            };
        };
    }

    public static <T, U, P> Function<T, Function<U, Predicate<P>>> curryingPredicate(TriPredicate<T, U, P> predicate) {
        return (t) -> {
            return (u) -> {
                return (p) -> {
                    return predicate.test(t, u, p);
                };
            };
        };
    }
}
