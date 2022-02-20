package zxf.java.functional.pattern;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, P, R> {
    R apply(T x, U y, P z);

    default <V> TriFunction<T, U, P, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, P p) -> {
            return after.apply(apply(t, u, p));
        };
    }
}
