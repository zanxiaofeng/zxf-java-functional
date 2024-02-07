package zxf.java.functional.core.function;

import zxf.java.functional.core.function.checked.CheckedBiConsumer;
import zxf.java.functional.core.function.checked.CheckedConsumer;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, P, R> {
    R apply(T x, U y, P z);

    default BiFunction<U, P, R> apply(final T x) {
        return (y, z) -> apply(x, y, z);
    }

    default Function<P, R> apply(final T x, final U y) {
        return (z) -> apply(x, y, z);
    }

    default <V> TriFunction<T, U, P, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, P p) -> {
            return after.apply(apply(t, u, p));
        };
    }
}
