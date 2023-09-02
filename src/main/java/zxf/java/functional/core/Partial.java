package zxf.java.functional.core;

import zxf.java.functional.core.function.TriPredicate;
import zxf.java.functional.core.function.checked.*;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class Partial {

    public static <T, U> CheckedConsumer<U> partialConsumer(T t, CheckedBiConsumer<T, U> consumer) {
        return (u) -> consumer.accept(t, u);
    }

    public static <T, U, P> CheckedBiConsumer<U, P> partialConsumer(T t, CheckedTriConsumer<T, U, P> consumer) {
        return (u, p) -> consumer.accept(t, u, p);
    }

    public static <T, U, P> CheckedConsumer<P> partialConsumer(T t, U u, CheckedTriConsumer<T, U, P> consumer) {
        return (p) -> consumer.accept(t, u, p);
    }


    public static <T, U, R> CheckedFunction<U, R> partialFunction(T t, CheckedBiFunction<T, U, R> function) {
        return (u) -> function.apply(t, u);
    }


    public static <T, U, P, R> CheckedBiFunction<U, P, R> partialFunction(T t, CheckedTriFunction<T, U, P, R> function) {
        return (u, p) -> function.apply(t, u, p);
    }

    public static <T, U, P, R> CheckedFunction<P, R> partialFunction(T t, U u, CheckedTriFunction<T, U, P, R> function) {
        return (p) -> function.apply(t, u, p);
    }


    public static <T, U> Predicate<U> partialPredicate(T t, BiPredicate<T, U> predicate) {
        return (u) -> predicate.test(t, u);
    }


    public static <T, U, P> BiPredicate<U, P> partialPredicate(T t, TriPredicate<T, U, P> predicate) {
        return (u, p) -> predicate.test(t, u, p);
    }

    public static <T, U, P> Predicate<P> partialPredicate(T t, U u, TriPredicate<T, U, P> predicate) {
        return (p) -> predicate.test(t, u, p);
    }
}
