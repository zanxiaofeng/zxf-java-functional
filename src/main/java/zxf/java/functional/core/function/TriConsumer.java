package zxf.java.functional.core.function;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface TriConsumer<T, U, P> {
    void accept(T x, U y, P z);

    default BiConsumer<U, P> apply(final T x) {
        return (y, z) -> accept(x, y, z);
    }

    default Consumer<P> apply(final T x, final U y) {
        return (z) -> accept(x, y, z);
    }

    default TriConsumer<T, U, P> andThen(TriConsumer<? super T, ? super U, ? super P> after) {
        Objects.requireNonNull(after);

        return (t, u, p) -> {
            accept(t, u, p);
            after.accept(t, u, p);
        };
    }
}